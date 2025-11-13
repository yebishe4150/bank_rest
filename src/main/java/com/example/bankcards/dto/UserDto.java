package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Информация о пользователе")
public class UserDto {

    @Schema(description = "ID пользователя", example = "21f4f8cf-0a78-4bd9-b5fb-08f7af81bae4")
    private UUID id;

    @Schema(description = "Username пользователя", example = "user@example.com")
    private String username;

    @Schema(description = "Роль пользователя", example = "ADMIN")
    private String role;
}
