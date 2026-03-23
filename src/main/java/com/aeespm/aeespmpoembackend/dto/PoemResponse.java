package com.aeespm.aeespmpoembackend.dto;

import com.aeespm.aeespmpoembackend.entity.Sede;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoemResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Sede sede;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
