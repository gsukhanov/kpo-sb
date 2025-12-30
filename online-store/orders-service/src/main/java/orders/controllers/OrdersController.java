package orders.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orders.dto.CreateOrderRequest;
import orders.dto.OrderResponse;
import orders.repositories.OrderRepository;
import orders.service.OrdersService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Создание заказа и просмотр статусов")
public class OrdersController {

    private final OrdersService ordersService;
    private final OrderRepository orderRepository;

    @PostMapping
    @Operation(summary = "Создать заказ (асинхронно запускает оплату через Kafka)")
    public ResponseEntity<OrderResponse> create(
            @RequestHeader("X-User-Id")
            @Parameter(description = "ID пользователя", required = true) int userId,
            @RequestHeader("Idempotency-Key")
            @Parameter(description = "Ключ идемпотентности для создания заказа", required = true) String idemKey,
            @Valid @RequestBody CreateOrderRequest req
    ) throws Exception {
        return ResponseEntity.ok(ordersService.createOrder(userId, idemKey, req.amount()));
    }

    @GetMapping
    @Operation(summary = "Список заказов пользователя")
    public ResponseEntity<List<OrderResponse>> list(
            @RequestHeader("X-User-Id")
            @Parameter(description = "ID пользователя", required = true) int userId
    ) {
        var res = orderRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(o -> new OrderResponse(o.getId(), o.getUserId(), o.getAmount(), o.getStatus()))
                .toList();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Статус/детали конкретного заказа")
    public ResponseEntity<OrderResponse> get(
            @RequestHeader("X-User-Id")
            @Parameter(description = "ID пользователя", required = true) int userId,
            @PathVariable long orderId
    ) {
        return ResponseEntity.ok(ordersService.getOrder(userId, orderId));
    }
}
