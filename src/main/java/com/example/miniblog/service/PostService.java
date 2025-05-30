package com.example.miniblog.service;

import com.example.miniblog.dto.PostDto;
import java.util.List;
import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.model.Post;
import com.example.miniblog.model.User;

public interface PostService {
    List<PostDto> getAllPosts();
    Post createPost(CreatePostRequest request);

    // Логика для удаления поста
    void deletePost(Long postId);

    boolean updatePost(Long id, CreatePostRequest request, String username);

    List<PostDto> getFilteredPosts(String query, Long categoryId, Long tagId);

    List<PostDto> getPostsByUser(User user);

}
