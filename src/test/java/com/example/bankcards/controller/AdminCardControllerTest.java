package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCardControllerTest extends AbstractTest {

    private User admin;
    private String adminToken;

    @BeforeEach
    void setUp() {
        admin = userRepository.save(
                User.builder()
                        .username(ADMIN_NAME)
                        .password(PASSWORD)
                        .role(Role.ADMIN)
                        .build()
        );
        adminToken = tokenOf(admin);
    }

    @Test
    void when_getAllCards_then_returnPagedCards() throws Exception {
        createCard(CardStatus.ACTIVE);
        createCard(CardStatus.BLOCKED);

        mockMvc.perform(get("/admin/cards?page=0&size=10")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.content").isArray());
    }

    @Test
    void when_activateCard_then_cardBecomesActive() throws Exception {
        Card card = createCard(CardStatus.BLOCKED);

        mockMvc.perform(post("/admin/cards/" + card.getId() + "/activate")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.status").value("ACTIVE"));

        Card updated = cardRepository.findById(card.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(CardStatus.ACTIVE);
    }

    @Test
    void when_blockCard_then_cardBecomesBlocked() throws Exception {
        Card card = createCard(CardStatus.ACTIVE);

        mockMvc.perform(post("/admin/cards/" + card.getId() + "/block")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.status").value("BLOCKED"));

        Card updated = cardRepository.findById(card.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(CardStatus.BLOCKED);
    }

    @Test
    void when_deleteCard_then_cardRemoved() throws Exception {
        Card card = createCard(CardStatus.ACTIVE);

        mockMvc.perform(delete("/admin/cards/" + card.getId())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Карта удалена"));

        assertThat(cardRepository.existsById(card.getId())).isFalse();
    }

    @Test
    void when_noToken_activate_then_401() throws Exception {
        mockMvc.perform(post("/admin/cards/" + UUID.randomUUID() + "/activate"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_noToken_block_then_401() throws Exception {
        mockMvc.perform(post("/admin/cards/" + UUID.randomUUID() + "/block"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_noToken_delete_then_401() throws Exception {
        mockMvc.perform(delete("/admin/cards/" + UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_userActivatesCard_then_403() throws Exception {
        User user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );
        String token = tokenOf(user);

        mockMvc.perform(post("/admin/cards/" + UUID.randomUUID() + "/activate")
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_userBlocksCard_then_403() throws Exception {
        User user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );
        String token = tokenOf(user);

        mockMvc.perform(post("/admin/cards/" + UUID.randomUUID() + "/block")
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_userDeletesCard_then_403() throws Exception {
        User user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );
        String token = tokenOf(user);

        mockMvc.perform(delete("/admin/cards/" + UUID.randomUUID())
                        .header("Authorization", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_blockNonExistingCard_then_404() throws Exception {
        mockMvc.perform(post("/admin/cards/" + UUID.randomUUID() + "/block")
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_deleteNonExistingCard_then_404() throws Exception {
        mockMvc.perform(delete("/admin/cards/" + UUID.randomUUID())
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_filterByStatus_then_onlyMatchingReturned() throws Exception {
        createCard(CardStatus.ACTIVE);
        createCard(CardStatus.BLOCKED);

        mockMvc.perform(get("/admin/cards?status=ACTIVE")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.content[*].status").value("ACTIVE"));
    }

    @Test
    void when_paginationRequested_then_returnCorrectPage() throws Exception {
        for (int i = 0; i < 15; i++) {
            createCard(CardStatus.ACTIVE);
        }

        mockMvc.perform(get("/admin/cards?page=1&size=10")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.number").value(1))
                .andExpect(jsonPath("$.body.size").value(10));
    }

    private Card createCard(CardStatus status) {
        String encrypted = cardEncryptor.encrypt("1234567812345678");

        return cardRepository.save(
                Card.builder()
                        .encryptedNumber(encrypted)
                        .expirationDate(LocalDate.now().plusYears(2))
                        .status(status)
                        .balance(java.math.BigDecimal.ZERO)
                        .currency("RUB")
                        .owner(admin)
                        .build()
        );
    }
}
