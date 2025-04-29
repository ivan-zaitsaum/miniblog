package com.example.miniblog.controller;

import com.example.miniblog.dto.PostDto;
import com.example.miniblog.model.User;
import com.example.miniblog.repository.UserRepository;
import com.example.miniblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserRepository userRepository;
    private final PostService postService;

    public UserRestController(UserRepository userRepository, PostService postService) {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    private User getCurrentUser() {
        String currentUsername = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Профиль пользователя
    @GetMapping("/me")
    public Map<String, Object> getCurrentUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("✅ USER AVATAR: " + user.getAvatar()); // Печатаем в консоль для проверки!

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("avatar", user.getAvatar()); // 👉 ДОБАВЛЯЕМ это!!

        return response;
    }

    // ✅ Посты пользователя
    @GetMapping("/posts")
    public List<PostDto> getPostsOfCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return postService.getPostsByUser(user);
    }

    @PostMapping("/avatar")
    public Map<String, Object> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String uploadsDir = "uploads/";
            Files.createDirectories(Paths.get(uploadsDir));

            String originalFilename = file.getOriginalFilename();
            String newFilename = username + "_" + System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = Paths.get(uploadsDir, newFilename);

            Files.copy(file.getInputStream(), filePath);

            user.setAvatar(newFilename);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("avatar", newFilename);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    @PutMapping("/me")
    public User updateProfile(@RequestBody Map<String, String> updates) {
        User user = getCurrentUser();
        String newUsername = updates.get("username");
        if (newUsername == null || newUsername.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username must not be empty");
        }
        // Проверка на уникальность
        if (userRepository.existsByUsername(newUsername)
                && !newUsername.equals(user.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already in use");
        }
        user.setUsername(newUsername);
        return userRepository.save(user);
    }
}
