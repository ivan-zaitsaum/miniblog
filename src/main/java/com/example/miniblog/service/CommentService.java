package com.example.miniblog.service;

import com.example.miniblog.dto.CommentDto;
import com.example.miniblog.dto.CreateCommentRequest;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsByPostId(Long postId);
    void addCommentToPost(Long postId, CreateCommentRequest request, String username);
}
