package com.example.blog.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CountStatResponse {
    private long userCount;
    private long postCount;
    private long commentCount;
}