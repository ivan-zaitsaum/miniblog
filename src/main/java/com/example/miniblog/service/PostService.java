package com.example.miniblog.service;

import com.example.miniblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Page<Post> listAll(String q, Long categoryId, Long tagId, Pageable pageable);
    Optional<Post> getById(Long id);
    Post save(Post post);
    void deleteById(Long id);
}
