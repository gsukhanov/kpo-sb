package gateway.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import gateway.clients.PaymentsClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/accounts")
@Tag(name = "Payments", description = "Gateway routes for Payments Service")
public class PaymentsGatewayController {
    private final PaymentsClient client;

    public PaymentsGatewayController(PaymentsClient client) {
        this.client = client;
    }

    @PostMapping
    @Operation(summary = "Create account (proxied to Payments Service)")
    public ResponseEntity<String> createAccount(@RequestHeader("X-User-Id") int userId) {
        HttpHeaders h = new HttpHeaders();
        h.add("X-User-Id", String.valueOf(userId));
        return client.createAccount(h);
    }

    @GetMapping("/balance")
    @Operation(summary = "Get balance (proxied to Payments Service)")
    public ResponseEntity<String> balance(@RequestHeader("X-User-Id") int userId) {
        HttpHeaders h = new HttpHeaders();
        h.add("X-User-Id", String.valueOf(userId));
        return client.balance(h);
    }

    @PostMapping("/topup")
    @Operation(summary = "Top up account (proxied to Payments Service)")
    public ResponseEntity<String> topup(
            @RequestHeader("X-User-Id") int userId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody JsonNode body
    ) {
        HttpHeaders h = new HttpHeaders();
        h.add("X-User-Id", String.valueOf(userId));
        h.add("Idempotency-Key", idempotencyKey);
        return client.topUp(h, body);
    }
}
