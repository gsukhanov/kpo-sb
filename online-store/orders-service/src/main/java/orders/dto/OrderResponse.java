package orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import orders.domains.OrderStatus;

public record OrderResponse(
        @Schema(description = "ID заказа", example = "1001")
        long orderId,
        @Schema(description = "ID пользователя", example = "42")
        int userId,
        @Schema(description = "Сумма заказа", example = "500")
        long amount,
        @Schema(description = "Статус заказа", example = "NEW")
        OrderStatus status
) {}
