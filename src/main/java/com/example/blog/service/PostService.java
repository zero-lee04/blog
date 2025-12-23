package com.example.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.domain.Category;
import com.example.blog.domain.Post;
import com.example.blog.domain.User;
import com.example.blog.dto.post.PostRequest;
import com.example.blog.dto.post.PostResponse;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository; // 나중에 카테고리 만들면 사용

    // 게시글 작성
    @Transactional
    public Long createPost(String email, PostRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .category(category)
                .build();

        return postRepository.save(post).getId();
    }

    // 게시글 목록 조회 (검색 기능 포함)
    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(String keyword, Pageable pageable) {
        Page<Post> posts;
        if (keyword == null || keyword.isBlank()) {
            posts = postRepository.findAllWithUser(pageable);
        } else {
            posts = postRepository.searchByKeyword(keyword, pageable);
        }
        return posts.map(PostResponse::new);
    }

    // 게시글 상세 조회
    @Transactional
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        post.increaseViews(); // 조회수 증가
        return new PostResponse(post);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(String email, Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        if (!post.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        
        // 카테고리 변경 로직 필요 시 추가
        post.update(request.getTitle(), request.getContent(), post.getCategory());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(String email, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        
        if (!post.getUser().getEmail().equals(email)) { // 관리자라면 삭제 가능하게 로직 추가 가능
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        
        postRepository.delete(post);
    }

    public Page<Post> findAllPaging(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return postRepository.findByTitleContaining(keyword, pageable);
        }
        return postRepository.findAll(pageable);
    }
}