package com.aeespm.aeespmpoembackend.controller;

import com.aeespm.aeespmpoembackend.dto.PoemRequest;
import com.aeespm.aeespmpoembackend.dto.PoemResponse;
import com.aeespm.aeespmpoembackend.service.PoemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/poems")
@RequiredArgsConstructor
public class PoemController {

    private final PoemService poemService;

    @GetMapping
    public ResponseEntity<List<PoemResponse>> getAllPoems() {
        return ResponseEntity.ok(poemService.getAllPoems());
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
}
