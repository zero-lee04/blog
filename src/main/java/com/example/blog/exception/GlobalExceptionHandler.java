package com.example.blog.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 잘못된 요청 (400 - POST_INVALID_INPUT) [cite: 81, 82]
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "POST_INVALID_INPUT", e.getMessage(), request, null);
    }

    // 2. 리소스를 찾을 수 없음 (404 - RESOURCE_NOT_FOUND) [cite: 82]
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청하신 데이터를 찾을 수 없습니다.", request, null);
    }

    // 3. 서버 내부 오류 (500 - INTERNAL_SERVER_ERROR) [cite: 82, 111]
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception e, HttpServletRequest request) {
        log.error("Internal Server Error: ", e); // 스택트레이스 로깅 (민감정보 제외는 출력 시 제어) [cite: 111]
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 문제가 발생했습니다.", request, null);
    }

    // 공통 응답 생성 메소드 [cite: 176, 177]
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String code, String message, HttpServletRequest request, Map<String, Object> details) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .status(status.value())
                .code(code)
                .message(message)
                .details(details)
                .build();
        return new ResponseEntity<>(response, status);
    }
}