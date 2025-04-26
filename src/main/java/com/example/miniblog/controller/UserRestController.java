package com.example.miniblog.controller;

import com.example.miniblog.dto.PostDto;
import com.example.miniblog.model.User;
import com.example.miniblog.repository.UserRepository;
import com.example.miniblog.service.PostService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ✅ Профиль пользователя
    @GetMapping("/me")
    public Map<String, Object> getCurrentUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

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
}
