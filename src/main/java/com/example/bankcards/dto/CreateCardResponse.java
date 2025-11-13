package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Ответ после создания карты")
public class CreateCardResponse {

    @Schema(description = "ID созданной карты")
    private UUID id;

    @Schema(description = "Маскированный номер карты")
    private String maskedNumber;
}
