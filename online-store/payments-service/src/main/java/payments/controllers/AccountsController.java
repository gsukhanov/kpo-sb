package payments.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payments.dto.AccountResponse;
import payments.dto.TopUpRequest;
import payments.service.AccountService;

@RestController
@RequestMapping("/api/payments/accounts")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Управление счетом пользователя")
public class AccountsController {

    public static final String HDR_USER_ID = "X-User-Id";
    public static final String HDR_IDEMPOTENCY = "Idempotency-Key";

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Создать счёт (если уже существует — вернуть существующий)")
    public ResponseEntity<AccountResponse> createAccount(@RequestHeader(HDR_USER_ID) int userId) {
        return ResponseEntity.ok(accountService.getOrCreateAccount(userId));
    }

    @GetMapping("/balance")
    @Operation(summary = "Получить баланс")
    public ResponseEntity<AccountResponse> balance(@RequestHeader(HDR_USER_ID) int userId) {
        return ResponseEntity.ok(accountService.getBalance(userId));
    }

    @PostMapping("/topup")
    @Operation(summary = "Пополнить баланс (идемпотентно по Idempotency-Key)")
    public ResponseEntity<AccountResponse> topUp(
            @RequestHeader(HDR_USER_ID) int userId,
            @RequestHeader(HDR_IDEMPOTENCY) String idempotencyKey,
            @Valid @RequestBody TopUpRequest req
    ) {
        return ResponseEntity.ok(accountService.topUp(userId, idempotencyKey, req.amount()));
    }
}
