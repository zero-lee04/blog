package com.example.blog.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "변경할 닉네임을 입력해주세요.")
    private String nickname;
}