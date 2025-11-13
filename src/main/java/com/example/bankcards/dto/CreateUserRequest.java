package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание пользователя")
public class CreateUserRequest {

    @NotBlank
    @Email
    @Schema(description = "Email пользователя", example = "user@example.com", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Пароль", example = "12345678", required = true)
    private String password;
}
