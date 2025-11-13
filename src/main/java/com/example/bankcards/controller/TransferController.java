package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.response.BaseResponse;
import com.example.bankcards.mapper.TransferMapper;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cards/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final TransferMapper transferMapper;
    private final UserService userService;

    @Operation(
            summary = "Перевод денег между картами пользователя",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Перевод выполнен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping
    public BaseResponse<TransferDto> transfer(
            @RequestBody @Valid TransferRequest request,
            Authentication auth
    ) {
        UUID userId = userService.getByUsername(auth.getName()).getId();

        TransferDto dto = transferMapper.toDto(
                transferService.send(
                        userId,
                        request.getFromCardId(),
                        request.getToCardId(),
                        request.getAmount()
                )
        );

        return BaseResponse.<TransferDto>builder()
                .id(UUID.randomUUID())
                .body(dto)
                .build();
    }
}
