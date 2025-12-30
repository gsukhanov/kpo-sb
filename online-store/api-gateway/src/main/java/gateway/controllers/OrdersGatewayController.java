package gateway.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import gateway.clients.OrdersClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Gateway routes for Orders Service")
public class OrdersGatewayController {
    private final OrdersClient client;

    public OrdersGatewayController(OrdersClient client) {
        this.client = client;
    }

    @PostMapping
    @Operation(summary = "Create order (proxied to Orders Service)")
    public ResponseEntity<String> createOrder(
            @RequestHeader("X-User-Id") int userId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody JsonNode body
    ) {
        HttpHeaders h = new HttpHeaders();
        h.add("X-User-Id", String.valueOf(userId));
        h.add("Idempotency-Key", idempotencyKey);
        return client.createOrder(h, body);
    }

    @GetMapping
    @Operation(summary = "List orders (proxied to Orders Service)")
    public ResponseEntity<String> listOrders(@RequestHeader("X-User-Id") int userId) {
        HttpHeaders h = new HttpHeaders();
        h.add("X-User-Id", String.valueOf(userId));
        return client.listOrders(h);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by id (proxied to Orders Service)")
    public ResponseEntity<String> getOrder(
            @RequestHeader("X-User-Id") int userId,
            @PathVariable String orderId
    ) {
        HttpHeaders h = new HttpHeaders();
        h.add("X-User-Id", String.valueOf(userId));
        return client.getOrder(h, orderId);
    }
}
