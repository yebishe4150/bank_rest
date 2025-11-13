package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход пользователя")
public class LoginRequest {

    @NotBlank
    @Email
    @Schema(description = "Email", example = "user@example.com", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Пароль", example = "12345678", required = true)
    private String password;
}
