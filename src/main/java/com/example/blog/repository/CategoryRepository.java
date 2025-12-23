package com.example.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 이름으로 조회 (중복 검사 등에 사용)
    Optional<Category> findByName(String name);
    
    // 이미 존재하는 이름인지 확인
    boolean existsByName(String name);
}