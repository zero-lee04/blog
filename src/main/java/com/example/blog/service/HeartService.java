package com.example.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.domain.Heart;
import com.example.blog.domain.Post;
import com.example.blog.domain.User;
import com.example.blog.repository.HeartRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 좋아요 토글 (누르면 ON, 다시 누르면 OFF)
    @Transactional
    public boolean toggleHeart(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        return heartRepository.findByUserAndPost(user, post)
                .map(heart -> {
                    // 이미 있으면 삭제 (좋아요 취소)
                    heartRepository.delete(heart);
                    return false; // 취소됨
                })
                .orElseGet(() -> {
                    // 없으면 추가 (좋아요)
                    heartRepository.save(new Heart(user, post));
                    return true; // 생성됨
                });
    }

    // 좋아요 개수 조회
    @Transactional(readOnly = true)
    public int countHearts(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        return heartRepository.countByPost(post);
    }
}