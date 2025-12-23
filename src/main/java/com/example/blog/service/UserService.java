package com.example.blog.service;

import com.example.blog.domain.User;
import com.example.blog.dto.user.UserResponse;
import com.example.blog.dto.user.UserUpdateRequest;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // [관리자] 전체 유저 조회
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    // [관리자] 유저 강제 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    // [일반] 내 정보 조회
    @Transactional(readOnly = true)
    public UserResponse getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return new UserResponse(user);
    }

    // [일반] 내 정보 수정
    @Transactional
    public void updateMyInfo(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        
        // 더티 체킹을 위한 update 메서드 호출 (User 엔티티에 메서드 추가 필요)
        user.updateNickname(request.getNickname());
    }
}