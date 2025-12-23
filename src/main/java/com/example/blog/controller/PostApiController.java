package com.example.blog.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.domain.Post;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.PostService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApiController {

    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping("/all")
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostResponse getPostDetail(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        return new PostResponse(post);
    }

    @GetMapping
    public ResponseEntity<?> getPosts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<Post> result = postService.findAllPaging(keyword, pageable);
        
        // 공지사항 응답 페이로드 규격 준수 [cite: 87, 88]
        return ResponseEntity.ok(Map.of(
            "content", result.getContent().stream().map(PostResponse::new).toList(),
            "page", result.getNumber(),
            "size", result.getSize(),
            "totalElements", result.getTotalElements(),
            "totalPages", result.getTotalPages(),
            "sort", pageable.getSort().toString()
        ));
    }

    @Getter
    public static class PostResponse {
        private Long id;
        private String title;
        private String content;
        private String author;
        private String category;
        private List<CommentResponse> comments;

        public PostResponse(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            
            // 작성자 이름 (Null 방어)
            this.author = (post.getUser() != null) ? post.getUser().getNickname() : "익명";
            
            // 카테고리 이름 (Null 방어)
            this.category = (post.getCategory() != null) ? post.getCategory().getName() : "없음";
            
            // 댓글 변환
            this.comments = post.getComments() != null ? 
                    post.getComments().stream()
                        .map(CommentResponse::new)
                        .collect(Collectors.toList()) : List.of();
        }
    }

    @Getter
    public static class CommentResponse {
        private String content;
        private String author;

        public CommentResponse(com.example.blog.domain.Comment comment) {
            this.content = comment.getContent();
            // 댓글 작성자 (Null 방어)
            this.author = (comment.getUser() != null) ? comment.getUser().getNickname() : "익명";
        }
    }
}