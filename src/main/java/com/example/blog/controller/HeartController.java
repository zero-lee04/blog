package com.example.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.service.HeartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Like API", description = "좋아요 기능")
@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @Operation(summary = "좋아요 토글 (누르면 ON/OFF)")
    @PostMapping
    public ResponseEntity<String> toggleHeart(Authentication authentication, @PathVariable Long postId) {
        boolean isLiked = heartService.toggleHeart(authentication.getName(), postId);
        return ResponseEntity.ok(isLiked ? "좋아요 처리되었습니다." : "좋아요가 취소되었습니다.");
    }

    @Operation(summary = "좋아요 개수 조회")
    @GetMapping("/count")
    public ResponseEntity<Integer> countHearts(@PathVariable Long postId) {
        return ResponseEntity.ok(heartService.countHearts(postId));
    }
}