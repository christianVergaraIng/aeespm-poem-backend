package com.aeespm.aeespmpoembackend.service;

import com.aeespm.aeespmpoembackend.dto.PoemRequest;
import com.aeespm.aeespmpoembackend.dto.PoemResponse;
import com.aeespm.aeespmpoembackend.entity.Poem;
import com.aeespm.aeespmpoembackend.repository.PoemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PoemService {

    private final PoemRepository poemRepository;

    public PoemResponse createPoem(PoemRequest request) {
        Poem poem = Poem.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .sede(request.getSede())
                .build();
        Poem savedPoem = poemRepository.save(poem);
        return mapToResponse(savedPoem);
    }

    public Page<PoemResponse> getAllPoems(Pageable pageable) {
        return poemRepository.findAll(pageable).map(this::mapToResponse);
    }

    public Optional<PoemResponse> getPoemById(Long id) {
        return poemRepository.findById(id)
                .map(this::mapToResponse);
    }

    public Optional<PoemResponse> updatePoem(Long id, PoemRequest request) {
        Optional<Poem> existingPoem = poemRepository.findById(id);
        if (existingPoem.isEmpty()) {
            return Optional.empty();
        }

        Poem poem = existingPoem.get();
        poem.setTitle(request.getTitle());
        poem.setContent(request.getContent());
        poem.setAuthor(request.getAuthor());
        poem.setSede(request.getSede());

        Poem updatedPoem = poemRepository.save(poem);
        return Optional.of(mapToResponse(updatedPoem));
    }

    public boolean deletePoem(Long id) {
        if (!poemRepository.existsById(id)) {
            return false;
        }
        poemRepository.deleteById(id);
        return true;
    }

    private PoemResponse mapToResponse(Poem poem) {
        return PoemResponse.builder()
                .id(poem.getId())
                .title(poem.getTitle())
                .content(poem.getContent())
                .author(poem.getAuthor())
                .sede(poem.getSede())
                .createdAt(poem.getCreatedAt())
                .updatedAt(poem.getUpdatedAt())
                .build();
    }
}
