package com.example.miniblog.service;

import com.example.miniblog.dto.PostDto;
import java.util.List;
import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.model.Post;

public interface PostService {
    List<PostDto> getAllPosts();
    Post createPost(CreatePostRequest request);
}
