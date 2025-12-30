package orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @Schema(description = "Сумма заказа (положительная)", example = "500")
        @NotNull @Min(1) Long amount
) {}
