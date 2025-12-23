package com.example.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 게시글 ID로 댓글 조회 + 작성자 정보(N+1 방지)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<Comment> findByPostId(@Param("postId") Long postId);
    // 기존 코드 안에 추가
    List<Comment> findAllByPost(Post post);
}