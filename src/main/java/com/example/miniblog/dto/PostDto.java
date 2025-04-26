package com.example.miniblog.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
    private Set<String> categoryNames;
    private Set<String> tagNames;

    public PostDto(Long id,
                   String title,
                   String content,
                   String authorUsername,
                   LocalDateTime createdAt,
                   Set<String> categoryNames,
                   Set<String> tagNames) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
        this.categoryNames = categoryNames;
        this.tagNames = tagNames;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorUsername() { return authorUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Set<String> getCategoryNames() { return categoryNames; }
    public Set<String> getTagNames() { return tagNames; }
}
