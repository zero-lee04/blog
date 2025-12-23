package com.example.blog.dto.post;

import java.time.LocalDateTime;

import com.example.blog.domain.Post;

import lombok.Getter;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final String category;
    private final int views;
    private final LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getNickname();
        this.category = post.getCategory() != null ? post.getCategory().getName() : "미분류";
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt();
    }
}