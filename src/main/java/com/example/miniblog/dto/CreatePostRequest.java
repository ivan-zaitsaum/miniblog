package com.example.miniblog.dto;

import java.util.Set;

public class CreatePostRequest {
    private String title;
    private String content;
    private Set<Long> categoryIds;
    private Set<Long> tagIds;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }

    public Set<Long> getTagIds() { return tagIds; }
    public void setTagIds(Set<Long> tagIds) { this.tagIds = tagIds; }
}
