package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends AbstractTest {

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
    void when_noToken_then_401() throws Exception {
        mockMvc.perform(get("/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_validToken_then_returnUserDto() throws Exception {
        mockMvc.perform(get("/me")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.body.username").value(USER_NAME))
                .andExpect(jsonPath("$.body.role").value("USER"));
    }
}
