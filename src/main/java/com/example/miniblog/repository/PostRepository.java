package com.example.miniblog.repository;

import com.example.miniblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByTitleContainingIgnoreCase(String q, Pageable p);
    Page<Post> findAllByCategories_Id(Long categoryId, Pageable p);
    Page<Post> findAllByTags_Id(Long tagId, Pageable p);
}
