package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(String username, String rawPassword);

    User getById(UUID id);

    User getByUsername(String username);

    List<User> getAll();

    User createAdmin(String username, String rawPassword);

    User updateRole(UUID id, Role newRole);
}
