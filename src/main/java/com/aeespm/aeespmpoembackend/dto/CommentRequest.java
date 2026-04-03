package com.aeespm.aeespmpoembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotNull(message = "El ID del poema es requerido")
    private Long poemId;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String content;
}
