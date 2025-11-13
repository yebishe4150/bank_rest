package com.example.bankcards.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Банковская карта пользователя")
public class Card {

    @Id
    @GeneratedValue
    @Schema(description = "Уникальный идентификатор карты", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "Зашифрованный номер карты", example = "eJyrVkrLz1eyUkpKLFKqBQA5AwMJ")
    private String encryptedNumber;

    @Column(nullable = false)
    @Schema(description = "Дата истечения срока действия карты", example = "2028-12-01")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Статус карты", example = "ACTIVE")
    private CardStatus status;

    @Column(nullable = false)
    @Schema(description = "Текущий баланс карты", example = "1500.75")
    private BigDecimal balance;

    @Column(nullable = false)
    @Schema(description = "Валюта")
    private String currency;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @Schema(description = "Владелец карты")
    private User owner;
}
