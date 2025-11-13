package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardEncryptor;
import com.example.bankcards.util.CardNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    private final CardEncryptor encryptor;
    private final CardNumberGenerator generator;

    @Override
    public Card createCard(UUID ownerId) {

        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        String plain = generator.generate();
        String encrypted = encryptor.encrypt(plain);

        Card card = Card.builder()
                .encryptedNumber(encrypted)
                .expirationDate(LocalDate.now().plusYears(3))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .currency("RUB")
                .owner(user)
                .build();

        return cardRepository.save(card);
    }

    @Override
    public Page<Card> getUserCards(UUID ownerId, CardStatus status, Pageable pageable) {
        if (status == null) {
            return cardRepository.findByOwnerId(ownerId, pageable);
        }
        return cardRepository.findByOwnerIdAndStatus(ownerId, status, pageable);
    }


    @Override
    public Card blockCard(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Карта не найдена"));

        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    @Override
    public Card changeBalance(UUID cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Карта не найдена"));

        card.setBalance(card.getBalance().add(amount));
        return cardRepository.save(card);
    }

    @Override
    public Card activateCard(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Карта не найдена"));

        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(UUID cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new NotFoundException("Карта не найдена");
        }
        cardRepository.deleteById(cardId);
    }

    @Override
    public Page<Card> getAllCards(CardStatus status, Pageable pageable) {
        if (status == null) {
            return cardRepository.findAll(pageable);
        }
        return cardRepository.findAllByStatus(status, pageable);
    }

}
