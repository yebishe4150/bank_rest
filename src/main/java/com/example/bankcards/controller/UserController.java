package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.response.BaseResponse;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Получить профиль текущего пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Профиль пользователя",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public BaseResponse<UserDto> me(Principal principal) {
        UserDto dto = userMapper.toDto(
                userService.getByUsername(principal.getName())
        );

        return BaseResponse.<UserDto>builder()
                .id(UUID.randomUUID())
                .body(dto)
                .build();
    }
}
