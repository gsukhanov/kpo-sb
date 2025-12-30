const $ = (id) => document.getElementById(id);
// Важно: многие ошибки вида "Failed to fetch" на фронте возникают из‑за того,
// что браузер блокирует ответ (CORS), даже если операция на бэкенде выполнилась.
// Поэтому фронт обращается напрямую к API Gateway по порту 8080.
// Gateway проброшен наружу в docker-compose: http://<host>:8080
const API_BASE = (window.__API_BASE__ || `${location.protocol}//${location.hostname}:8080`);


function now() {
  return new Date().toISOString();
}

function log(msg, obj) {
  const el = $("log");
  const line = typeof obj === "undefined" ? msg : `${msg}\n${JSON.stringify(obj, null, 2)}`;
  el.textContent = `[${now()}] ${line}\n\n` + el.textContent;
}

function getUserId() {
  const raw = localStorage.getItem("userId");
  const uid = raw ? parseInt(raw, 10) : NaN;
  if (!uid || uid <= 0) throw new Error("No userId set. Please enter userId first.");
  return uid;
}

function userHeaders(extra = {}) {
  const uid = getUserId();
  return { "X-User-Id": String(uid), ...extra };
}

async function apiFetch(path, opts = {}) {
  const res = await fetch(API_BASE + path, opts);
  const ct = (res.headers.get("content-type") || "").toLowerCase();
  const raw = await res.text();

  let body = raw;
  const looksJson = raw && (raw.trim().startsWith("{") || raw.trim().startsWith("["));
  if ((ct.includes("application/json") || looksJson) && raw) {
    try {
      body = JSON.parse(raw);
    } catch {
      body = raw;
    }
  }

  if (!res.ok) {
    const err = new Error(`HTTP ${res.status}`);
    err.status = res.status;
    err.body = body;
    throw err;
  }
  return body;
}

function setOrdersTable(rows) {
  const tbody = $("ordersTable").querySelector("tbody");
  tbody.innerHTML = "";
  for (const r of rows) {
    const tr = document.createElement("tr");
    tr.innerHTML = `<td>${r.orderId ?? r.id}</td><td>${r.userId}</td><td>${r.amount}</td><td>${r.status}</td>`;
    tbody.appendChild(tr);
  }
}

$("btnGen").addEventListener("click", () => {
  const v = `web-${Date.now()}-${Math.random().toString(16).slice(2)}`;
  $("idem").value = v;
  log("Generated Idempotency-Key", { key: v });
});

$("btnCreateAcc").addEventListener("click", async () => {
  try {
    const body = await apiFetch("/api/payments/accounts", {
      method: "POST",
      headers: userHeaders(),
    });
    log("Create account OK", body);
  } catch (e) {
    log("Create account FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

$("btnBalance").addEventListener("click", async () => {
  try {
    const body = await apiFetch("/api/payments/accounts/balance", {
      method: "GET",
      headers: userHeaders(),
    });
    log("Balance OK", body);
  } catch (e) {
    log("Balance FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

$("btnTopup").addEventListener("click", async () => {
  try {
    const amount = parseInt($("topupAmount").value, 10);
    if (!amount || amount <= 0) throw new Error("amount must be positive");
    const key = $("idem").value.trim();
    if (!key) throw new Error("Idempotency-Key is required for topup");
    const body = await apiFetch("/api/payments/accounts/topup", {
      method: "POST",
      headers: userHeaders({ "Idempotency-Key": key, "Content-Type": "application/json" }),
      body: JSON.stringify({ amount }),
    });
    log("Topup OK", body);
  } catch (e) {
    log("Topup FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

$("btnCreateOrder").addEventListener("click", async () => {
  try {
    const amount = parseInt($("orderAmount").value, 10);
    if (!amount || amount <= 0) throw new Error("amount must be positive");
    const key = $("idem").value.trim();
    if (!key) throw new Error("Idempotency-Key is required for create order");
    const body = await apiFetch("/api/orders", {
      method: "POST",
      headers: userHeaders({ "Idempotency-Key": key, "Content-Type": "application/json" }),
      body: JSON.stringify({ amount }),
    });
    $("orderId").value = (body.orderId ?? body.id);
    log("Create order OK", body);
  } catch (e) {
    log("Create order FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

$("btnListOrders").addEventListener("click", async () => {
  try {
    const body = await apiFetch("/api/orders", {
      method: "GET",
      headers: userHeaders(),
    });
    setOrdersTable(body);
    log("List orders OK", { count: body.length });
  } catch (e) {
    log("List orders FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

$("btnGetOrder").addEventListener("click", async () => {
  try {
    const id = parseInt($("orderId").value, 10);
    if (!id || id <= 0) throw new Error("orderId must be positive");
    const body = await apiFetch(`/api/orders/${id}`, {
      method: "GET",
      headers: userHeaders(),
    });
    log("Get order OK", body);
  } catch (e) {
    log("Get order FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

$("btnPollOrder").addEventListener("click", async () => {
  try {
    const id = parseInt($("orderId").value, 10);
    if (!id || id <= 0) throw new Error("orderId must be positive");
    for (let i = 0; i < 10; i++) {
      const body = await apiFetch(`/api/orders/${id}`, {
        method: "GET",
        headers: userHeaders(),
      });
      log(`Poll ${i + 1}/10`, body);
      await new Promise((r) => setTimeout(r, 1000));
    }
  } catch (e) {
    log("Poll FAIL", { message: e.message, status: e.status, body: e.body });
  }
});

function setLoggedIn(uid) {
  $("currentUserId").textContent = String(uid);
  $("authCard").style.display = "none";
  $("contextCard").style.display = "";
  $("appRoot").style.display = "";
}

function setLoggedOut() {
  $("authCard").style.display = "";
  $("contextCard").style.display = "none";
  $("appRoot").style.display = "none";
}

function initAuth() {
  const raw = localStorage.getItem("userId");
  const uid = raw ? parseInt(raw, 10) : NaN;
  if (uid && uid > 0) setLoggedIn(uid);
  else setLoggedOut();

  $("btnSaveUser").addEventListener("click", () => {
    const v = parseInt($("authUserId").value, 10);
    if (!v || v <= 0) {
      log("Auth FAIL", { message: "userId must be a positive number" });
      return;
    }
    localStorage.setItem("userId", String(v));
    setLoggedIn(v);
    log("Auth OK", { userId: v });
  });

  $("btnChangeUser").addEventListener("click", () => {
    localStorage.removeItem("userId");
    setLoggedOut();
    log("Switched user", { userId: null });
  });

  $("authUserId").addEventListener("keydown", (e) => {
    if (e.key === "Enter") $("btnSaveUser").click();
  });
}

document.addEventListener("DOMContentLoaded", initAuth);
