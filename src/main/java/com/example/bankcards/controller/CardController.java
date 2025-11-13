package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardResponse;
import com.example.bankcards.dto.response.BaseResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final UserService userService;

    @Operation(
            summary = "Создать карту пользователю (ADMIN)",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Карта создана",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/create/{userId}")
    public BaseResponse<CreateCardResponse> createCard(@PathVariable UUID userId) {
        return BaseResponse.<CreateCardResponse>builder()
                .id(UUID.randomUUID())
                .body(cardMapper.toCreateResponse(cardService.createCard(userId)))
                .build();
    }

    @Operation(summary = "Получить свои карты")
    @ApiResponse(
            responseCode = "200",
            description = "Список карт",
            content = @Content(schema = @Schema(implementation = BaseResponse.class))
    )
    @GetMapping("/my")
    public BaseResponse<Page<CardDto>> getMyCards(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) CardStatus status
    ) {
        UUID userId = userService.getByUsername(auth.getName()).getId();
        Pageable pageable = PageRequest.of(page, size);

        Page<CardDto> result = cardService.getUserCards(userId, status, pageable)
                .map(cardMapper::toDto);

        return BaseResponse.<Page<CardDto>>builder()
                .id(UUID.randomUUID())
                .body(result)
                .build();
    }

    @Operation(summary = "Заблокировать карту")
    @ApiResponse(
            responseCode = "200",
            description = "Карта заблокирована",
            content = @Content(schema = @Schema(implementation = BaseResponse.class))
    )
    @PostMapping("/{cardId}/block")
    public BaseResponse<CardDto> block(@PathVariable UUID cardId) {
        return BaseResponse.<CardDto>builder()
                .id(UUID.randomUUID())
                .body(cardMapper.toDto(cardService.blockCard(cardId)))
                .build();
    }

    @Operation(summary = "Изменить баланс карты")
    @ApiResponse(
            responseCode = "200",
            description = "Баланс изменён",
            content = @Content(schema = @Schema(implementation = BaseResponse.class))
    )
    @PostMapping("/{cardId}/balance")
    public BaseResponse<CardDto> changeBalance(
            @PathVariable UUID cardId,
            @RequestParam BigDecimal amount
    ) {
        return BaseResponse.<CardDto>builder()
                .id(UUID.randomUUID())
                .body(cardMapper.toDto(cardService.changeBalance(cardId, amount)))
                .build();
    }
}
