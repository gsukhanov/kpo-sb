package payments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record TopUpRequest(
        @Schema(description = "Сумма пополнения (положительная)", example = "1000")
        @Min(value = 1, message = "amount must be >= 1")
        long amount
) {}
