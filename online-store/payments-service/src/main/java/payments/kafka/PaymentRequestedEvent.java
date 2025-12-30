package payments.kafka;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentRequestedEvent(
        @Schema(description = "ID заказа", example = "1001")
        long orderId,
        @Schema(description = "ID пользователя", example = "42")
        int userId,
        @Schema(description = "Сумма списания (положительная)", example = "500")
        long amount
) {}
