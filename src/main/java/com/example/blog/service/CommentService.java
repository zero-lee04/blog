package com.example.blog.service;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.domain.User;
import com.example.blog.dto.comment.CommentRequest;
import com.example.blog.dto.comment.CommentResponse;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public Long createComment(String email, Long postId, CommentRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .build();

        return commentRepository.save(comment).getId();
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        
        // CommentRepository에 findAllByPost 메서드가 필요합니다. (아래 3번 참고)
        return commentRepository.findAllByPost(post).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public void updateComment(String email, Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.update(request.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(String email, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}