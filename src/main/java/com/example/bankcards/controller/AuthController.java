package com.example.bankcards.controller;

import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.response.BaseResponse;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.AuthService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Регистрация пользователя",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь зарегистрирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/register")
    public BaseResponse<AuthResponse> register(@RequestBody @Valid RegisterRequest req) {
        return BaseResponse.<AuthResponse>builder()
                .id(UUID.randomUUID())
                .body(authService.register(req))
                .build();
    }

    @Operation(
            summary = "Авторизация",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/login")
    public BaseResponse<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        return BaseResponse.<AuthResponse>builder()
                .id(UUID.randomUUID())
                .body(authService.login(req))
                .build();
    }

    @Operation(
            summary = "Регистрация администратора",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Админ создан",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/register-admin")
    public BaseResponse<UserDto> registerAdmin(@RequestBody @Valid RegisterRequest request) {
        return BaseResponse.<UserDto>builder()
                .id(UUID.randomUUID())
                .body(
                        userMapper.toDto(
                                userService.createAdmin(
                                        request.getUsername(),
                                        request.getPassword()
                                )
                        )
                )
                .build();
    }
}
