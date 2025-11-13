package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Информация о переводе")
public class TransferDto {

    @Schema(description = "ID перевода")
    private UUID id;

    @Schema(description = "ID карты отправителя")
    private UUID fromCardId;

    @Schema(description = "ID карты получателя")
    private UUID toCardId;

    @Schema(description = "Сумма перевода", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Дата создания перевода")
    private LocalDateTime createdAt;

    @Schema(description = "Валюта перевода")
    private String currency;
}
