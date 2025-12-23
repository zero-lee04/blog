package com.example.blog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.domain.Role;
import com.example.blog.domain.User;
import com.example.blog.dto.auth.LoginRequest;
import com.example.blog.dto.auth.SignupRequest;
import com.example.blog.dto.auth.TokenResponse;
import com.example.blog.repository.UserRepository;
import com.example.blog.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public Long signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .nickname(request.getNickname())
                .role(Role.ROLE_USER) // 기본 권한 USER
                .build();

        return userRepository.save(user).getId();
    }

    // 로그인
    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // TODO: Redis에 Refresh Token 저장하는 로직이 여기에 들어가야 함 (심화 과제)

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}