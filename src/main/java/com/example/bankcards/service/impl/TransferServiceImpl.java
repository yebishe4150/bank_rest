package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.ForbiddenException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    @Override
    @Transactional
    public Transfer send(UUID userId, UUID fromCardId, UUID toCardId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Сумма должна быть положительной");
        }

        if (fromCardId.equals(toCardId)) {
            throw new BadRequestException("Нельзя переводить самому себе");
        }

        Card from = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new NotFoundException("Карта отправителя не найдена"));

        Card to = cardRepository.findById(toCardId)
                .orElseThrow(() -> new NotFoundException("Карта получателя не найдена"));

        if (!from.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Можно переводить только со своих карт");
        }

        if (from.getStatus() != CardStatus.ACTIVE || to.getStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException("Обе карты должны быть активны");
        }

        LocalDate now = LocalDate.now();
        if (from.getExpirationDate().isBefore(now) || to.getExpirationDate().isBefore(now)) {
            throw new BadRequestException("Одна из карт просрочена");
        }

        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new BadRequestException("Переводы между разными валютами запрещены");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Недостаточно средств");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepository.save(from);
        cardRepository.save(to);

        Transfer transfer = Transfer.builder()
                .fromCard(from)
                .toCard(to)
                .amount(amount)
                .currency(from.getCurrency())
                .createdAt(LocalDateTime.now())
                .build();

        return transferRepository.save(transfer);
    }
}
