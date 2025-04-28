package com.example.miniblog.controller;

import com.example.miniblog.dto.CommentDto;
import com.example.miniblog.dto.CreateCommentRequest;
import com.example.miniblog.service.CommentService;
import com.example.miniblog.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@CrossOrigin(origins = "*")
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@PathVariable Long postId,
                                           @RequestBody CreateCommentRequest request) {
        // вместо приведения principal к User:
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        commentService.addCommentToPost(postId, request, username);
        return ResponseEntity.ok().build();
    }
}
