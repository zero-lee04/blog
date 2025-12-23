package com.example.blog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.blog.domain.Post; // import 추가
import com.example.blog.dto.stat.DailyPostStatInterface;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 제목 또는 내용으로 검색 + 페이징 + N+1 방지(Fetch Join)
    @Query("SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN FETCH p.category " +
           "WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 전체 조회 + 페이징 + N+1 방지
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN FETCH p.category",
           countQuery = "SELECT count(p) FROM Post p")
    Page<Post> findAllWithUser(Pageable pageable);

    // [추가] 일별 게시글 작성 수 통계 (MySQL 문법 기준)
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m-%d') as date, COUNT(*) as count " +
                   "FROM posts " +
                   "GROUP BY date " +
                   "ORDER BY date DESC " +
                   "LIMIT 7", nativeQuery = true)
    List<DailyPostStatInterface> findDailyPostStats();

    Page<Post> findByTitleContaining(String keyword, Pageable pageable);

    
}