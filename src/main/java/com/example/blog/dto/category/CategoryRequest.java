package com.example.blog.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name;
}