package com.example.blog.controller;

import com.example.blog.dto.post.PostRequest;
import com.example.blog.dto.post.PostResponse;
import com.example.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post API", description = "게시글 관련 API")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ResponseEntity<Long> createPost(Authentication authentication, @RequestBody @Valid PostRequest request) {
        // Authentication 객체에서 로그인한 유저의 이메일 추출
        String email = authentication.getName();
        Long postId = postService.createPost(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @Operation(summary = "게시글 목록 조회 (검색/페이징)")
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(keyword, pageable));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(Authentication authentication, @PathVariable Long id, @RequestBody @Valid PostRequest request) {
        postService.updatePost(authentication.getName(), id, request);
        return ResponseEntity.ok("수정 성공");
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(Authentication authentication, @PathVariable Long id) {
        postService.deletePost(authentication.getName(), id);
        return ResponseEntity.ok("삭제 성공");
    }
}