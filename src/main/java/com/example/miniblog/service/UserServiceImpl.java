package com.example.miniblog.service;

import com.example.miniblog.dto.UserRegistrationDto;
import com.example.miniblog.model.User;
import com.example.miniblog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void registerNewUser(UserRegistrationDto dto) {
        if (repo.existsByUsername(dto.getUsername()))
            throw new IllegalArgumentException("Username already taken");
        if (repo.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("Email already in use");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // TODO: обработать через PasswordEncoder
        user.setEmail(dto.getEmail());
        repo.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}
