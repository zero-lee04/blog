package com.example.blog.dto.user;

import java.time.LocalDateTime;

import com.example.blog.domain.User;

import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String role;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }
}