package payments.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountResponse(
        @Schema(description = "ID пользователя", example = "42")
        int userId,
        @Schema(description = "Баланс (целое число, например в копейках)", example = "1500")
        long balance
) {}
