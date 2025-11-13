package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {

    @NotBlank
    @Email
    @Schema(description = "Email", example = "newuser@example.com", required = true)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    @Schema(description = "Пароль (от 6 до 50)", example = "qwerty123", required = true)
    private String password;
}
