package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.response.BaseResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Администрирование пользователей")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Получить список всех пользователей",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @GetMapping("/users")
    public BaseResponse<List<UserDto>> getUsers() {

        List<UserDto> users = userService.getAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

        return BaseResponse.<List<UserDto>>builder()
                .id(UUID.randomUUID())
                .body(users)
                .build();
    }

    @Operation(
            summary = "Создать пользователя",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь создан",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/users/create")
    public BaseResponse<UserDto> createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        UserDto dto = userMapper.toDto(
                userService.createUser(request.getUsername(), request.getPassword())
        );

        return BaseResponse.<UserDto>builder()
                .id(UUID.randomUUID())
                .body(dto)
                .build();
    }

    @Operation(
            summary = "Изменить роль пользователя",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Роль изменена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/users/{id}/role")
    public BaseResponse<UserDto> changeRole(
            @PathVariable UUID id,
            @RequestParam Role role
    ) {
        UserDto dto = userMapper.toDto(
                userService.updateRole(id, role)
        );

        return BaseResponse.<UserDto>builder()
                .id(UUID.randomUUID())
                .body(dto)
                .build();
    }
}
