package com.example.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.blog.util.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 해제 (Rest API에서는 브라우저 세션을 안 쓰므로 보통 끕니다)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. CORS 설정 제거됨 (프론트엔드 브라우저가 없으므로 필요 없음)

            // 3. 주소별 권한 설정
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // 지금은 테스트를 위해 모든 요청 허용
            );
        
        // 나중에 JWT 인증 필터를 적용할 때 아래 주석을 해제하면 됩니다.
        // .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}