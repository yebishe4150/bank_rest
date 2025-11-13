package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с JWT-токеном")
public class AuthResponse {

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
}
