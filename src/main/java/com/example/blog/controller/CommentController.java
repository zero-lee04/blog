package com.example.blog.controller;

import com.example.blog.dto.comment.CommentRequest;
import com.example.blog.dto.comment.CommentResponse;
import com.example.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment API", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> createComment(
            Authentication authentication,
            @PathVariable Long postId,
            @RequestBody @Valid CommentRequest request) {
        Long commentId = commentService.createComment(authentication.getName(), postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @Operation(summary = "특정 게시글의 댓글 목록 조회")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> updateComment(
            Authentication authentication,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest request) {
        commentService.updateComment(authentication.getName(), commentId, request);
        return ResponseEntity.ok("댓글 수정 성공");
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            Authentication authentication,
            @PathVariable Long commentId) {
        commentService.deleteComment(authentication.getName(), commentId);
        return ResponseEntity.ok("댓글 삭제 성공");
    }
}