package com.example.miniblog.dto;

import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;

    public PostDto(Long id, String title, String content, String authorUsername, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
    }

    // getters only
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorUsername() { return authorUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
