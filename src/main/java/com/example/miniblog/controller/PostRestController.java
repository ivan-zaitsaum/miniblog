package com.example.miniblog.controller;

import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.dto.PostDto;
import com.example.miniblog.model.Post;
import com.example.miniblog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getAllPosts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId
    ) {
        if (q != null || categoryId != null || tagId != null) {
            return postService.getFilteredPosts(q, categoryId, tagId);
        }
        return postService.getAllPosts();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest request) {
        Post created = postService.createPost(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @RequestBody CreatePostRequest request) {
        // берём имя пользователя из Authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean updated = postService.updatePost(id, request, username);
        if (!updated) {
            return ResponseEntity.status(403)
                    .body("You can edit only your own posts.");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
