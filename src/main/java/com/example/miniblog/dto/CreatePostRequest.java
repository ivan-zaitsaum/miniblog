package com.example.miniblog.dto;

public class CreatePostRequest {
    private String title;
    private String content;
    private String authorUsername;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorUsername() { return authorUsername; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
}
