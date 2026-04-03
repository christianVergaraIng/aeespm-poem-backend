package com.aeespm.aeespmpoembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private String nickname;
    private String content;
    private Long poemId;
    private String poemTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
