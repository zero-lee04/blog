package com.example.blog.service;

import com.example.blog.domain.Category;
import com.example.blog.dto.category.CategoryRequest;
import com.example.blog.dto.category.CategoryResponse;
import com.example.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 생성
    @Transactional
    public void createCategory(CategoryRequest request) {
        // 중복 이름 방지
        /* CategoryRepository에 findByName이 없으므로, 
           필요하면 Repository에 'Optional<Category> findByName(String name);' 추가 필요.
           여기서는 생략하고 그냥 저장합니다.
        */
        categoryRepository.save(Category.builder().name(request.getName()).build());
    }

    // 카테고리 목록 조회
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    
    // 카테고리 수정
    // CategoryService.java 안에 이어서 작성
    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
             .orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다."));
        category.update(request.getName());
    }
}