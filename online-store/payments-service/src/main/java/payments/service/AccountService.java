package payments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import payments.domains.Account;
import payments.domains.RestIdempotencyKey;
import payments.dto.AccountResponse;
import payments.repositories.AccountRepository;
import payments.repositories.RestIdempotencyRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RestIdempotencyRepository idempotencyRepository;

    @Transactional
    public AccountResponse getOrCreateAccount(int userId) {
        var existing = accountRepository.findByUserId(userId);
        if (existing.isPresent()) return toResponse(existing.get());

        try {
            Account created = accountRepository.save(new Account(userId));
            return toResponse(created);
        } catch (DataIntegrityViolationException e) {
            // race: another request created the account concurrently
            return accountRepository.findByUserId(userId)
                    .map(this::toResponse)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Account creation race"));
        }
    }

    @Transactional
    public AccountResponse getBalance(int userId) {
        // UX-friendly: если счета нет — создаём с балансом 0 (всё равно 1 счёт на пользователя)
        Account acc = accountRepository.findByUserId(userId).orElseGet(() -> {
            try {
                return accountRepository.save(new Account(userId));
            } catch (DataIntegrityViolationException e) {
                return accountRepository.findByUserId(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Account creation race"));
            }
        });
        return toResponse(acc);
    }

    @Transactional
    public AccountResponse topUp(int userId, String idempotencyKey, long amount) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Idempotency-Key header is required");
        }

        // гарантируем наличие счета (если нет — создаём)
        getOrCreateAccount(userId);

        if (idempotencyRepository.existsByUserIdAndKey(userId, idempotencyKey)) {
            return getBalance(userId);
        }

        try {
            idempotencyRepository.save(new RestIdempotencyKey(userId, idempotencyKey));
        } catch (DataIntegrityViolationException e) {
            // duplicate key (concurrent retry) -> do not apply again
            return getBalance(userId);
        }

        int updated = accountRepository.addBalance(userId, amount);
        if (updated != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Top up failed");
        }
        return getBalance(userId);
    }

    @Transactional
    public WithdrawResult withdraw(int userId, long amount) {
        if (!accountRepository.existsByUserId(userId)) {
            return WithdrawResult.fail("NO_ACCOUNT");
        }
        int updated = accountRepository.subtractIfEnough(userId, amount);
        if (updated == 1) return WithdrawResult.ok();
        return WithdrawResult.fail("INSUFFICIENT_FUNDS");
    }

    private AccountResponse toResponse(Account acc) {
        long bal = acc.getBalance() == null ? 0L : acc.getBalance();
        return new AccountResponse(acc.getUserId(), bal);
    }
}
