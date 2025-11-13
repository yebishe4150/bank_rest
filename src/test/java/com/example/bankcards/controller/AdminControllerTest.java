package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest extends AbstractTest {

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
    void when_getUsersWithAdmin_then_200() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").isArray())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void when_getUsersWithoutToken_then_401() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_getUsersAsUser_then_403() throws Exception {
        User user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        String userToken = tokenOf(user);

        mockMvc.perform(get("/admin/users")
                        .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_createUserAsAdmin_then_200() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername(NEW_USER_EMAIL);
        req.setPassword(DEFAULT_NEW_PASSWORD);

        mockMvc.perform(post("/admin/users/create")
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.username").value(NEW_USER_EMAIL))
                .andExpect(jsonPath("$.id").exists());

        assertThat(userRepository.findByUsername(NEW_USER_EMAIL)).isPresent();
    }

    @Test
    void when_createUserWithoutToken_then_401() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername(NEW_USER_EMAIL);
        req.setPassword(DEFAULT_NEW_PASSWORD);

        mockMvc.perform(post("/admin/users/create")
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_createUserAsUser_then_403() throws Exception {
        User user = userRepository.save(
                User.builder()
                        .username(USER_NAME)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        String userToken = tokenOf(user);

        CreateUserRequest req = new CreateUserRequest();
        req.setUsername(OTHER_USER_EMAIL);
        req.setPassword(DEFAULT_NEW_PASSWORD);

        mockMvc.perform(post("/admin/users/create")
                        .header("Authorization", userToken)
                        .contentType("application/json")
                        .content(toJson(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_changeRoleAsAdmin_then_200() throws Exception {
        User target = userRepository.save(
                User.builder()
                        .username(TARGET_EMAIL)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        mockMvc.perform(post("/admin/users/" + target.getId() + "/role")
                        .header("Authorization", adminToken)
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.role").value("ADMIN"))
                .andExpect(jsonPath("$.id").exists());

        User updated = userRepository.findById(target.getId()).orElseThrow();
        assertThat(updated.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void when_changeRoleNonExistingUser_then_404() throws Exception {
        mockMvc.perform(post("/admin/users/" + UUID.randomUUID() + "/role")
                        .header("Authorization", adminToken)
                        .param("role", "ADMIN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_changeRoleWithoutToken_then_401() throws Exception {
        mockMvc.perform(post("/admin/users/" + UUID.randomUUID() + "/role")
                        .param("role", "ADMIN"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_changeRoleAsUser_then_403() throws Exception {
        User user = userRepository.save(
                User.builder()
                        .username(OTHER_USER_EMAIL)
                        .password(PASSWORD)
                        .role(Role.USER)
                        .build()
        );

        String userToken = tokenOf(user);

        mockMvc.perform(post("/admin/users/" + UUID.randomUUID() + "/role")
                        .header("Authorization", userToken)
                        .param("role", "ADMIN"))
                .andExpect(status().isForbidden());
    }
}
