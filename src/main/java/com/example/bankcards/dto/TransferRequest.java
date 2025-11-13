package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Запрос на перевод между картами")
public class TransferRequest {

    @NotNull
    @Schema(description = "ID карты-отправителя", required = true)
    private UUID fromCardId;

    @NotNull
    @Schema(description = "ID карты-получателя", required = true)
    private UUID toCardId;

    @NotNull
    @Positive
    @Schema(description = "Сумма перевода (должна быть > 0)", example = "50.00", required = true)
    private BigDecimal amount;
}
