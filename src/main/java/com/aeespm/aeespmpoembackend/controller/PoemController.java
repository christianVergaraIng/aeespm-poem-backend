package com.aeespm.aeespmpoembackend.controller;

import com.aeespm.aeespmpoembackend.dto.PoemRequest;
import com.aeespm.aeespmpoembackend.dto.PoemResponse;
import com.aeespm.aeespmpoembackend.service.PoemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/poems")
@RequiredArgsConstructor
public class PoemController {

    private final PoemService poemService;

    @GetMapping
    public ResponseEntity<Page<PoemResponse>> getAllPoems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(poemService.getAllPoems(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoemResponse> getPoemById(@PathVariable Long id) {
        Optional<PoemResponse> poem = poemService.getPoemById(id);
        if (poem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(poem.get());
    }

    @PostMapping
    public ResponseEntity<PoemResponse> createPoem(@Valid @RequestBody PoemRequest request) {
        PoemResponse poem = poemService.createPoem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(poem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoemResponse> updatePoem(@PathVariable Long id, @Valid @RequestBody PoemRequest request) {
        Optional<PoemResponse> poem = poemService.updatePoem(id, request);
        if (poem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(poem.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoem(@PathVariable Long id) {
        if (!poemService.deletePoem(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PoemResponse> uploadPoemAudio(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        try {
            Optional<PoemResponse> poem = poemService.attachAudio(id, file);
            if (poem.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(poem.get());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/audio")
    public ResponseEntity<byte[]> getPoemAudio(@PathVariable Long id) {
        Optional<PoemService.PoemAudioData> audioData = poemService.getPoemAudio(id);
        if (audioData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PoemService.PoemAudioData audio = audioData.get();
        MediaType mediaType = audio.contentType() != null
                ? MediaType.parseMediaType(audio.contentType())
                : MediaType.APPLICATION_OCTET_STREAM;

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(audio.bytes().length);

        if (audio.fileName() != null && !audio.fileName().isBlank()) {
            responseBuilder.header("Content-Disposition", ContentDisposition.inline().filename(audio.fileName()).build().toString());
        }

        return responseBuilder.body(audio.bytes());
    }

    @DeleteMapping("/{id}/audio")
    public ResponseEntity<Void> deletePoemAudio(@PathVariable Long id) {
        if (!poemService.deletePoemAudio(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
