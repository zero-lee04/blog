package com.example.blog.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder // 빌더 패턴 사용 가능하게 함
@AllArgsConstructor // @Builder가 내부적으로 사용하는 생성자
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    // N+1 문제 방지를 위해 LAZY 로딩 필수 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // 글 삭제 시 댓글도 삭제
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String content, User user, Category category) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.category = category;
        this.views = 0;
    }

    // 수정 메서드 (Dirty Checking 용)
    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
    
    public void increaseViews() {
        this.views++;
    }
}