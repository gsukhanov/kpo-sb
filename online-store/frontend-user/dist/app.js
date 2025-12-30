const $ = (id) => document.getElementById(id);
// Чтобы исключить проблемы с CORS/проксированием, обращаемся напрямую к API Gateway
// (docker-compose пробрасывает его на порт 8080).
const API_BASE = (window.__API_BASE__ || `${location.protocol}//${location.hostname}:8080`);


function toast(msg, kind = "info") {
  const el = $("toast");
  if (!el) return;
  el.textContent = msg;
  el.style.display = "";
  el.dataset.kind = kind;
  setTimeout(() => { el.style.display = "none"; }, 2600);
}

function setMsg(id, msg) {
  const el = $(id);
  if (!el) return;
  el.textContent = msg || "";
}

function uuid() {
  return (globalThis.crypto && crypto.randomUUID)
    ? crypto.randomUUID()
    : `k-${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

function getUserId() {
  const raw = localStorage.getItem("userId");
  const uid = raw ? parseInt(raw, 10) : NaN;
  if (!uid || uid <= 0) throw new Error("Сначала войдите (userId).");
  return uid;
}

function setUser(uid) {
  localStorage.setItem("userId", String(uid));
  $("userLabel").textContent = String(uid);
  $("loginCard").style.display = "none";
  $("app").style.display = "";
}

function clearUser() {
  localStorage.removeItem("userId");
  $("userLabel").textContent = "—";
  $("loginCard").style.display = "";
  $("app").style.display = "none";
}

async function apiFetch(path, opts = {}) {
  const res = await fetch(API_BASE + path, opts);

  const text = await res.text();
  const trimmed = (text || "").trim();
  const ct = (res.headers.get("content-type") || "").toLowerCase();

  let body = text;
  const looksJson = trimmed.startsWith("{") || trimmed.startsWith("[");
  if ((ct.includes("application/json") || looksJson) && trimmed.length) {
    try { body = JSON.parse(trimmed); } catch { body = text; }
  }

  if (!res.ok) {
    const err = new Error(`HTTP ${res.status}`);
    err.status = res.status;
    err.body = body;
    throw err;
  }
  return body;
}

function headers(extra = {}) {
  return { "X-User-Id": String(getUserId()), ...extra };
}

async function createAccount() {
  setMsg("accMsg", "");
  try {
    await apiFetch("/api/payments/accounts", { method: "POST", headers: headers() });
    toast("Счёт создан");
  } catch (e) {
    // Даже если бек уже создал счёт, но ответ вернулся с ошибкой — покажем детали
    setMsg("accMsg", `Создание счёта: ${e.status || ""} ${formatErrBody(e.body)}`.trim());
    toast("Ошибка создания счёта", "error");
  } finally {
    // обновление баланса не должно превращать успешное создание в "ошибку"
    refreshBalance().catch(() => {});
  }
}

async function refreshBalance() {
  setMsg("accMsg", "");
  try {
    const b = await apiFetch("/api/payments/accounts/balance", { method: "GET", headers: headers() });
    $("balanceValue").textContent = (b && b.balance != null) ? b.balance : "—";
    return b;
  } catch (e) {
    $("balanceValue").textContent = "—";
    if (e.status === 404 || e.status === 400) {
      setMsg("accMsg", "Счёт не найден. Нажмите «Создать счёт».");
    } else {
      setMsg("accMsg", `Не удалось получить баланс: ${e.status || ""} ${formatErrBody(e.body)}`.trim());
    }
  }
}

async function topup() {
  setMsg("accMsg", "");
  const amount = parseInt($("topupAmount").value, 10);
  if (!amount || amount <= 0) { toast("Введите сумму", "error"); return; }

  // Idempotency-Key скрыт от пользователя
  const idem = uuid();

  try {
    const b = await apiFetch("/api/payments/accounts/topup", {
      method: "POST",
      headers: headers({ "Idempotency-Key": idem, "Content-Type": "application/json" }),
      body: JSON.stringify({ amount })
    });
    $("balanceValue").textContent = (b && b.balance != null) ? b.balance : "—";
    toast("Баланс пополнен");
  } catch (e) {
    setMsg("accMsg", `Не удалось пополнить: ${e.status || ""} ${formatErrBody(e.body)}`.trim());
    toast("Ошибка пополнения", "error");
  }
}

function renderOrders(rows) {
  const tbody = $("ordersTable").querySelector("tbody");
  tbody.innerHTML = "";
  for (const r of rows || []) {
    const tr = document.createElement("tr");
    const btn = document.createElement("button");
    btn.textContent = "Открыть";
    btn.className = "ghost";
    btn.addEventListener("click", () => {
      $("orderId").value = (r.orderId ?? r.id);
      getOrder();
    });

    tr.innerHTML = `<td>${r.orderId ?? r.id}</td><td>${r.amount}</td><td>${r.status}</td>`;
    const td = document.createElement("td");
    td.appendChild(btn);
    tr.appendChild(td);
    tbody.appendChild(tr);
  }
}

async function listOrders() {
  setMsg("ordersMsg", "");
  try {
    const rows = await apiFetch("/api/orders", { method: "GET", headers: headers() });
    renderOrders(rows);
    setMsg("ordersMsg", rows && rows.length ? `Заказов: ${rows.length}` : "Пока заказов нет.");
  } catch (e) {
    setMsg("ordersMsg", `Не удалось получить заказы: ${e.status || ""} ${formatErrBody(e.body)}`.trim());
    toast("Ошибка", "error");
  }
}

async function createOrder() {
  setMsg("ordersMsg", "");
  const amount = parseInt($("orderAmount").value, 10);
  if (!amount || amount <= 0) { toast("Введите сумму заказа", "error"); return; }

  const idem = uuid();

  try {
    const o = await apiFetch("/api/orders", {
      method: "POST",
      headers: headers({ "Idempotency-Key": idem, "Content-Type": "application/json" }),
      body: JSON.stringify({ amount })
    });
    const oid = (o && (o.orderId ?? o.id));
    if (oid != null) $("orderId").value = oid;
    toast(oid != null ? `Заказ #${oid} создан` : "Заказ создан");
  } catch (e) {
    setMsg("ordersMsg", `Создание заказа: ${e.status || ""} ${formatErrBody(e.body)}`.trim());
    toast("Ошибка создания заказа", "error");
  } finally {
    // обновление списка — best-effort
    listOrders().catch(() => {});
  }
}

async function getOrder() {
  setMsg("ordersMsg", "");
  const id = parseInt($("orderId").value, 10);
  if (!id || id <= 0) { toast("Введите ID заказа", "error"); return; }

  try {
    const o = await apiFetch(`/api/orders/${id}`, { method: "GET", headers: headers() });
    setMsg("ordersMsg", `Заказ #${(o.orderId ?? o.id)}: статус ${o.status}, сумма ${o.amount}`);
    toast(`Статус: ${o.status}`);
    await listOrders();
  } catch (e) {
    if (e.status === 404) setMsg("ordersMsg", "Заказ не найден.");
    else setMsg("ordersMsg", `Не удалось получить заказ: ${e.status || ""} ${formatErrBody(e.body)}`.trim());
    toast("Ошибка", "error");
  }
}

function formatErrBody(body) {
  if (body == null) return "";
  if (typeof body === "string") return body;
  try { return JSON.stringify(body); } catch { return String(body); }
}

function init() {
  // login state
  const raw = localStorage.getItem("userId");
  const uid = raw ? parseInt(raw, 10) : NaN;
  if (uid && uid > 0) setUser(uid); else clearUser();

  $("btnLogin").addEventListener("click", () => {
    const v = parseInt($("loginUserId").value, 10);
    if (!v || v <= 0) { toast("Введите корректный userId", "error"); return; }
    setUser(v);
    toast("Вы вошли");
    refreshBalance().catch(() => {});
    listOrders().catch(() => {});
  });
  $("loginUserId").addEventListener("keydown", (e) => { if (e.key === "Enter") $("btnLogin").click(); });

  $("btnLogout").addEventListener("click", () => { clearUser(); toast("Пользователь сброшен"); });

  $("btnCreateAcc").addEventListener("click", createAccount);
  $("btnRefreshBalance").addEventListener("click", refreshBalance);
  $("btnTopup").addEventListener("click", topup);

  $("btnCreateOrder").addEventListener("click", createOrder);
  $("btnListOrders").addEventListener("click", listOrders);
  $("btnGetOrder").addEventListener("click", getOrder);

  if (uid && uid > 0) {
    refreshBalance().catch(() => {});
    listOrders().catch(() => {});
  }
}

document.addEventListener("DOMContentLoaded", init);
