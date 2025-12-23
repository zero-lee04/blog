package com.example.blog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.stat.CountStatResponse;
import com.example.blog.dto.stat.DailyPostStatInterface;
import com.example.blog.service.StatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin Stats API", description = "관리자 통계 API")
@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @Operation(summary = "전체 게시물/회원 수 요약")
    @GetMapping("/counts")
    public ResponseEntity<CountStatResponse> getCountStats() {
        return ResponseEntity.ok(statService.getCountStats());
    }

    @Operation(summary = "최근 7일간 게시글 작성 통계")
    @GetMapping("/daily-posts")
    public ResponseEntity<List<DailyPostStatInterface>> getDailyPostStats() {
        return ResponseEntity.ok(statService.getDailyPostStats());
    }
}