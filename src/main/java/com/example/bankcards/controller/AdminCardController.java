package com.example.bankcards.controller;

import com.example.bankcards.dto.response.BaseResponse;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/cards")
@RequiredArgsConstructor
@Tag(name = "Администрирование карт")
public class AdminCardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    @Operation(
            summary = "Получить список всех карт",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Список карт",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @GetMapping
    public BaseResponse<Page<CardDto>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) CardStatus status
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<CardDto> result = cardService.getAllCards(status, pageable)
                .map(cardMapper::toDto);

        return BaseResponse.<Page<CardDto>>builder()
                .id(UUID.randomUUID())
                .body(result)
                .build();
    }

    @Operation(
            summary = "Активировать карту",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Карта активирована",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/{cardId}/activate")
    public BaseResponse<CardDto> activateCard(@PathVariable UUID cardId) {
        return BaseResponse.<CardDto>builder()
                .id(UUID.randomUUID())
                .body(cardMapper.toDto(cardService.activateCard(cardId)))
                .build();
    }

    @Operation(
            summary = "Заблокировать карту",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Карта заблокирована",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @PostMapping("/{cardId}/block")
    public BaseResponse<CardDto> blockCard(@PathVariable UUID cardId) {
        return BaseResponse.<CardDto>builder()
                .id(UUID.randomUUID())
                .body(cardMapper.toDto(cardService.blockCard(cardId)))
                .build();
    }

    @Operation(
            summary = "Удалить карту",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Карта удалена",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    )
    @DeleteMapping("/{cardId}")
    public BaseResponse<Void> deleteCard(@PathVariable UUID cardId) {
        cardService.deleteCard(cardId);

        return BaseResponse.<Void>builder()
                .id(UUID.randomUUID())
                .message("Карта удалена")
                .build();
    }
}
