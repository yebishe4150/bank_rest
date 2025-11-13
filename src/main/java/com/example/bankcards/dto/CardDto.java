package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Информация о банковской карте")
public class CardDto {

    @Schema(description = "ID карты", example = "e1c1e3bb-7c62-4f3d-84f3-576e247e0d11")
    private UUID id;

    @Schema(description = "Маскированный номер", example = "**** **** **** 1234")
    private String maskedNumber;

    @Schema(description = "Дата истечения", example = "2027-10-01")
    private LocalDate expirationDate;

    @Schema(description = "Статус карты", example = "ACTIVE")
    private String status;

    @Schema(description = "Баланс", example = "1500.50")
    private BigDecimal balance;

    @Schema(description = "Валюта карты")
    private String currency;
}
