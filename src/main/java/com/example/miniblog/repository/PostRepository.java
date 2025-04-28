package com.example.miniblog.repository;

import com.example.miniblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long userId);
    @Query("""
        SELECT DISTINCT p 
        FROM Post p 
        LEFT JOIN p.categories c 
        LEFT JOIN p.tags t 
        WHERE (:q IS NULL 
               OR LOWER(p.title)   LIKE LOWER(CONCAT('%', :q, '%')) 
               OR LOWER(p.content) LIKE LOWER(CONCAT('%', :q, '%')))
          AND (:categoryId IS NULL OR c.id = :categoryId)
          AND (:tagId      IS NULL OR t.id = :tagId)
        ORDER BY p.createdAt DESC
        """)
    List<Post> findFiltered(
            @Param("q")           String q,
            @Param("categoryId")  Long categoryId,
            @Param("tagId")       Long tagId
    );
}

