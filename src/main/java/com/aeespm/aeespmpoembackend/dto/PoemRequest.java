package com.aeespm.aeespmpoembackend.dto;

import com.aeespm.aeespmpoembackend.entity.Sede;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemRequest {
    @NotBlank(message = "Titulo es requerido")
    private String title;

    @NotBlank(message = "Poema es requerido")
    private String content;

    @NotBlank(message = "Autor es requerido")
    private String author;

    private Sede sede;
}
