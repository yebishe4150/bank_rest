package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface CardService {

    Card createCard(UUID ownerId);

    Page<Card> getUserCards(UUID ownerId, CardStatus status, Pageable pageable);

    Card blockCard(UUID cardId);

    Card changeBalance(UUID cardId, BigDecimal amount);

    Card activateCard(UUID cardId);

    void deleteCard(UUID cardId);

    Page<Card> getAllCards(CardStatus status, Pageable pageable);

}
