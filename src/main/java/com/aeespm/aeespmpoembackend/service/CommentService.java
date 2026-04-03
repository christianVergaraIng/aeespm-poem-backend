package com.aeespm.aeespmpoembackend.service;

import com.aeespm.aeespmpoembackend.dto.CommentRequest;
import com.aeespm.aeespmpoembackend.dto.CommentResponse;
import com.aeespm.aeespmpoembackend.entity.Comment;
import com.aeespm.aeespmpoembackend.entity.Poem;
import com.aeespm.aeespmpoembackend.repository.CommentRepository;
import com.aeespm.aeespmpoembackend.repository.PoemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PoemRepository poemRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        Poem poem = poemRepository.findById(request.getPoemId())
                .orElseThrow(() -> new RuntimeException("Poema no encontrado con ID: " + request.getPoemId()));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .poem(poem)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPoemId(Long poemId) {
        return commentRepository.findByPoemIdOrderByCreatedAtDesc(poemId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado con ID: " + id));
        return mapToResponse(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comentario no encontrado con ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .poemId(comment.getPoem().getId())
                .poemTitle(comment.getPoem().getTitle())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
