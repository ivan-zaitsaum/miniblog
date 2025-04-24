package com.example.miniblog.service;

import com.example.miniblog.dto.UserRegistrationDto;

import com.example.miniblog.model.User;
import java.util.Optional;

public interface UserService {
    void registerNewUser(UserRegistrationDto dto);
    Optional<User> findByUsername(String username);
}
