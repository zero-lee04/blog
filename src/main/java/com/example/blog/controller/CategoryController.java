package com.example.blog.controller;

import com.example.blog.dto.category.CategoryRequest;
import com.example.blog.dto.category.CategoryResponse;
import com.example.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category API", description = "카테고리 관리 (관리자 전용)")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회 (누구나)")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @Operation(summary = "카테고리 생성 (관리자)")
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity.ok("카테고리 생성 완료");
    }

    @Operation(summary = "카테고리 수정 (관리자)")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest request) {
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok("카테고리 수정 완료");
    }

    @Operation(summary = "카테고리 삭제 (관리자)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("카테고리 삭제 완료");
    }
}