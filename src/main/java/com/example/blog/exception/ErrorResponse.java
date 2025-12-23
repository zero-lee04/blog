package com.example.blog.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp; // 에러 발생 시각 (ISO 8601) 
    private final String path;             // 요청 경로 
    private final int status;              // HTTP 상태 코드 
    private final String code;             // 시스템 내부 에러 코드 (대문자+언더스코어) 
    private final String message;          // 사용자에게 전달할 메시지 
    private final Map<String, Object> details; // 필드별 오류 등 상세 사유 
}