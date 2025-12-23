package com.example.blog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.domain.Category;
import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.domain.Role;
import com.example.blog.domain.User;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;

@Component
public class DataInit implements CommandLineRunner {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;     // 추가됨
    private final CategoryRepository categoryRepository; // 추가됨
    private final BCryptPasswordEncoder passwordEncoder;

    // 생성자 주입 (모든 리포지토리 다 가져오기)
    public DataInit(PostRepository postRepository, CommentRepository commentRepository,
                    UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1. 임시 유저 만들기 (User 파일 필드명에 맞춰 수정 필요할 수 있음)
        // 만약 User에 @Builder가 있다면 이렇게 쓰세요:
        User user = User.builder()
                .nickname("테스트유저")      // User.java에 'name' 필드가 있다고 가정
                .email("test@test.com") // User.java에 'email' 필드가 있다고 가정
                .password(passwordEncoder.encode("1234")) // "1234"를 해시화해서 저장
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        // 2. 임시 카테고리 만들기
        // 만약 Category에 @Builder가 있다면:
        Category category = Category.builder()
                .name("일상")          // Category.java에 'name' 필드가 있다고 가정
                .build();
        categoryRepository.save(category);

        // 3. 글 20개 생성
        for (int i = 1; i <= 20; i++) {
            Post post = Post.builder()
                    .title("제목 " + i)
                    .content("내용입니다. " + i)
                    .user(user)         // ★ 방금 만든 유저 넣기
                    .category(category) // ★ 방금 만든 카테고리 넣기
                    .build();
            
            postRepository.save(post);

            // 4. 댓글 5개씩 생성
            for (int j = 1; j <= 5; j++) {
                Comment comment = Comment.builder()
                        .content("댓글 " + j + " (글번호:" + i + ")")
                        .post(post) // 어떤 글의 댓글인지
                        .user(user) // ★ [추가됨] 누가 썼는지 (아까 만든 user 재사용)
                        .build();
                commentRepository.save(comment);
            }
        }
        
        System.out.println("✅ 데이터 생성 완료! (유저, 카테고리, 글, 댓글 포함)");
    }
}