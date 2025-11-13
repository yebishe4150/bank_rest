package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

class CardControllerTest extends AbstractTest {

    private User user;
    private User admin;
    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        admin = userRepository.save(
                User.builder()
                        .username(ADMIN_NAME)
                        .password(PASSWORD)
                        .role(Role.ADMIN)
                        .build()
        );

        userToken = tokenOf(user);
        adminToken = tokenOf(admin);
    }

    @Test
    void when_getMyCardsWithToken_then_returnCards() throws Exception {
        createCard(user, CardStatus.ACTIVE);

        mockMvc.perform(get("/cards/my")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.content").isArray());
    }

    @Test
    void when_getMyCardsWithoutToken_then_401() throws Exception {
        mockMvc.perform(get("/cards/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_adminCreatesCard_then_success() throws Exception {
        mockMvc.perform(post("/cards/create/" + user.getId())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").exists())
                .andExpect(jsonPath("$.body.maskedNumber").exists());
    }

    @Test
    void when_userCreatesCard_then_403() throws Exception {
        mockMvc.perform(post("/cards/create/" + user.getId())
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_adminBlocksCard_then_statusBecomesBlocked() throws Exception {
        Card card = createCard(user, CardStatus.ACTIVE);

        mockMvc.perform(post("/cards/" + card.getId() + "/block")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.status").value("BLOCKED"));

        assertThat(cardRepository.findById(card.getId()).orElseThrow().getStatus())
                .isEqualTo(CardStatus.BLOCKED);
    }

    @Test
    void when_userBlocksCard_then_403() throws Exception {
        Card card = createCard(user, CardStatus.ACTIVE);

        mockMvc.perform(post("/cards/" + card.getId() + "/block")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_blockNonExistingCard_then_404() throws Exception {
        mockMvc.perform(post("/cards/" + UUID.randomUUID() + "/block")
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_adminChangesBalance_then_ok() throws Exception {
        Card card = createCard(user, CardStatus.ACTIVE);

        mockMvc.perform(post("/cards/" + card.getId() + "/balance?amount=100")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.balance").value(100));
    }

    @Test
    void when_userChangesBalance_then_403() throws Exception {
        Card card = createCard(user, CardStatus.ACTIVE);

        mockMvc.perform(post("/cards/" + card.getId() + "/balance?amount=50")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_changeBalanceOfNonExistingCard_then_404() throws Exception {
        mockMvc.perform(post("/cards/" + UUID.randomUUID() + "/balance?amount=50")
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound());
    }

    private Card createCard(User owner, CardStatus status) {
        return cardRepository.save(
                Card.builder()
                        .encryptedNumber(cardEncryptor.encrypt("1234567812345678"))
                        .expirationDate(LocalDate.now().plusYears(2))
                        .status(status)
                        .balance(BigDecimal.ZERO)
                        .currency("RUB")
                        .owner(owner)
                        .build()
        );
    }
}
