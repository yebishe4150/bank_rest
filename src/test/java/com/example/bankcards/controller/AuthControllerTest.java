package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerTest extends AbstractTest {

    @Test
    void when_registerNewUser_then_tokenReturned() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(NEW_USER);
        req.setPassword(ANY_PASSWORD);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.token").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void when_validCredentials_then_loginReturnsToken() throws Exception {
        userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(passwordEncoder.encode(ANY_PASSWORD))
                        .role(Role.USER)
                        .build()
        );

        LoginRequest req = new LoginRequest();
        req.setUsername(USER_NAME);
        req.setPassword(ANY_PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.token").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void when_invalidPassword_then_loginReturns401() throws Exception {
        userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        LoginRequest req = new LoginRequest();
        req.setUsername(USER_NAME);
        req.setPassword("wrong-password");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_registerAdmin_then_userCreatedWithAdminRole() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(NEW_ADMIN);
        req.setPassword(ANY_PASSWORD);

        mockMvc.perform(post("/auth/register-admin")
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.username").value(NEW_ADMIN))
                .andExpect(jsonPath("$.body.role").value("ADMIN"))
                .andExpect(jsonPath("$.id").exists());

        User created = userRepository.findByUsername(NEW_ADMIN).orElseThrow();
        assertThat(created.getRole()).isEqualTo(Role.ADMIN);
    }
}
