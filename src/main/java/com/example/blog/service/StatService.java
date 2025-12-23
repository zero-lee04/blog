package com.example.blog.service;

import lombok.*;
import com.example.blog.dto.stat.CountStatResponse;
import com.example.blog.dto.stat.DailyPostStatInterface;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 전체 카운트 요약
    @Transactional(readOnly = true)
    public CountStatResponse getCountStats() {
        return CountStatResponse.builder()
                .userCount(userRepository.count())
                .postCount(postRepository.count())
                .commentCount(commentRepository.count())
                .build();
    }

    // 일별 게시글 통계 (최근 7일)
    @Transactional(readOnly = true)
    public List<DailyPostStatInterface> getDailyPostStats() {
        return postRepository.findDailyPostStats();
    }
}