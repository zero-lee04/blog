package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder // 빌더 패턴 사용 가능하게 함
@AllArgsConstructor // @Builder가 내부적으로 사용하는 생성자
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }
}