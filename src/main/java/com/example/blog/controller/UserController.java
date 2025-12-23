package com.example.blog.controller;

import com.example.blog.dto.user.UserResponse;
import com.example.blog.dto.user.UserUpdateRequest;
import com.example.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getMyInfo(authentication.getName()));
    }

    @Operation(summary = "내 정보(닉네임) 수정")
    @PutMapping("/me")
    public ResponseEntity<String> updateMyInfo(Authentication authentication, @RequestBody @Valid UserUpdateRequest request) {
        userService.updateMyInfo(authentication.getName(), request);
        return ResponseEntity.ok("내 정보 수정 완료");
    }
}