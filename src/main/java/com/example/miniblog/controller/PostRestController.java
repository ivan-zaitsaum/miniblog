package com.example.miniblog.controller;

import com.example.miniblog.dto.PostDto;
import com.example.miniblog.service.PostService;
import org.springframework.web.bind.annotation.*;
import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.model.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest request) {
        Post created = postService.createPost(request);
        return ResponseEntity.ok(created);
    }
}
