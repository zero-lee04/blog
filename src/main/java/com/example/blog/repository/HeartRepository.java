package com.example.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.domain.Heart;
import com.example.blog.domain.Post;
import com.example.blog.domain.User;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    // 유저가 특정 글에 좋아요를 눌렀는지 확인
    Optional<Heart> findByUserAndPost(User user, Post post);
    
    // 게시글의 좋아요 개수 세기
    int countByPost(Post post);
}