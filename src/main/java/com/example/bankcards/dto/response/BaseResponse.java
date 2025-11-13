package com.example.bankcards.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Базовый API-ответ")
public class BaseResponse<T> {

    @Schema(description = "Уникальный идентификатор ответа", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "Тело ответа")
    private T body;

    @Schema(description = "Сообщение об ошибке", example = "Invalid request")
    private String message;
}
