package com.example.blog.dto.comment;

import java.time.LocalDateTime;

import com.example.blog.domain.Comment;

import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
    }
}