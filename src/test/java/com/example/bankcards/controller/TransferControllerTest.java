package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransferControllerTest extends AbstractTest {

    private User user;
    private String userToken;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        userToken = tokenOf(user);
    }

    @Test
    void when_validTransfer_then_success() throws Exception {
        Card from = newCard(user, BigDecimal.valueOf(100), "RUB");
        Card to = newCard(user, BigDecimal.ZERO, "RUB");

        TransferRequest req = new TransferRequest();
        req.setFromCardId(from.getId());
        req.setToCardId(to.getId());
        req.setAmount(BigDecimal.valueOf(50));

        mockMvc.perform(post("/cards/transfer")
                        .contentType("application/json")
                        .header("Authorization", userToken)
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.body.fromCardId").value(from.getId().toString()))
                .andExpect(jsonPath("$.body.toCardId").value(to.getId().toString()))
                .andExpect(jsonPath("$.body.amount").value(50));
    }

    @Test
    void when_noToken_then_401() throws Exception {
        TransferRequest req = new TransferRequest();
        req.setFromCardId(UUID.randomUUID());
        req.setToCardId(UUID.randomUUID());
        req.setAmount(BigDecimal.TEN);

        mockMvc.perform(post("/cards/transfer")
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_transferringFromAnotherUsersCard_then_403() throws Exception {
        User other = userRepository.save(
                User.builder()
                        .username(OTHER_USER_EMAIL)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        Card othersCard = newCard(other, BigDecimal.valueOf(100), "RUB");
        Card myCard = newCard(user, BigDecimal.ZERO, "RUB");

        TransferRequest req = new TransferRequest();
        req.setFromCardId(othersCard.getId());
        req.setToCardId(myCard.getId());
        req.setAmount(BigDecimal.TEN);

        mockMvc.perform(post("/cards/transfer")
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_cardNotFound_then_404() throws Exception {
        Card to = newCard(user, BigDecimal.ZERO, "RUB");

        TransferRequest req = new TransferRequest();
        req.setFromCardId(UUID.randomUUID());
        req.setToCardId(to.getId());
        req.setAmount(BigDecimal.TEN);

        mockMvc.perform(post("/cards/transfer")
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_amountNegative_then_400() throws Exception {
        Card from = newCard(user, BigDecimal.valueOf(100), "RUB");
        Card to = newCard(user, BigDecimal.ZERO, "RUB");

        TransferRequest req = new TransferRequest();
        req.setFromCardId(from.getId());
        req.setToCardId(to.getId());
        req.setAmount(BigDecimal.valueOf(-5));

        mockMvc.perform(post("/cards/transfer")
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_transferBetweenDifferentCurrencies_then_400() throws Exception {
        Card from = newCard(user, BigDecimal.valueOf(100), "RUB");
        Card to = newCard(user, BigDecimal.ZERO, "USD");

        TransferRequest req = new TransferRequest();
        req.setFromCardId(from.getId());
        req.setToCardId(to.getId());
        req.setAmount(BigDecimal.TEN);

        mockMvc.perform(post("/cards/transfer")
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isBadRequest());
    }


    private Card newCard(User owner, BigDecimal balance, String currency) {
        return cardRepository.save(
                Card.builder()
                        .encryptedNumber(cardEncryptor.encrypt("1234567812345678"))
                        .expirationDate(LocalDate.now().plusYears(2))
                        .status(CardStatus.ACTIVE)
                        .balance(balance)
                        .currency(currency)
                        .owner(owner)
                        .build()
        );
    }
}
