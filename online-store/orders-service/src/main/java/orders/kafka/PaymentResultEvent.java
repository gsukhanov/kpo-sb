package orders.kafka;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentResultEvent(
        @Schema(description = "ID заказа", example = "1001")
        long orderId,
        @Schema(description = "ID пользователя", example = "42")
        int userId,
        @Schema(description = "Успешно ли списание", example = "true")
        boolean success,
        @Schema(description = "Причина ошибки (если success=false)", example = "INSUFFICIENT_FUNDS")
        String reason
) {}
