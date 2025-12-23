package com.example.blog.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Health Check", description = "서버 상태 확인")
@RestController
public class HealthController {


    @GetMapping("/api/health")
    public String healthCheck() {
        return "Spring Boot 연결 성공! 🚀";
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "version", "1.0.0",
            "serverTime", LocalDateTime.now().toString(),
            "description", "Blog API Server is running"
        );
    }
}