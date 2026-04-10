package com.aeespm.aeespmpoembackend.service;

import com.aeespm.aeespmpoembackend.dto.PoemRequest;
import com.aeespm.aeespmpoembackend.dto.PoemResponse;
import com.aeespm.aeespmpoembackend.entity.Poem;
import com.aeespm.aeespmpoembackend.repository.PoemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PoemService {

    private final PoemRepository poemRepository;
    private final AudioCompressionService audioCompressionService;

    @Value("${app.audio.max-size-bytes:15728640}")
    private long maxAudioSizeBytes;

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

    @Transactional
    public Optional<PoemResponse> attachAudio(Long poemId, MultipartFile file) {
        Optional<Poem> existingPoem = poemRepository.findById(poemId);
        if (existingPoem.isEmpty()) {
            return Optional.empty();
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de audio es requerido");
        }
        if (file.getSize() > maxAudioSizeBytes) {
            throw new IllegalArgumentException("El audio excede el tamaño máximo permitido");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new IllegalArgumentException("Solo se permiten archivos de audio");
        }

        byte[] originalBytes = getFileBytes(file);
        byte[] compressedBytes = audioCompressionService.compress(originalBytes);
        boolean useCompression = compressedBytes.length < originalBytes.length;

        Poem poem = existingPoem.get();
        poem.setAudioData(useCompression ? compressedBytes : originalBytes);
        poem.setAudioCompression(useCompression ? "GZIP" : "NONE");
        poem.setAudioContentType(contentType);
        poem.setAudioFilename(file.getOriginalFilename());
        poem.setAudioOriginalSize(originalBytes.length);
        poem.setAudioCompressedSize(poem.getAudioData().length);

        Poem savedPoem = poemRepository.save(poem);
        return Optional.of(mapToResponse(savedPoem));
    }

    @Transactional(readOnly = true)
    public Optional<PoemAudioData> getPoemAudio(Long poemId) {
        return poemRepository.findById(poemId)
                .filter(poem -> poem.getAudioData() != null && poem.getAudioData().length > 0)
                .map(poem -> {
                    byte[] audioBytes = "GZIP".equalsIgnoreCase(poem.getAudioCompression())
                            ? audioCompressionService.decompress(poem.getAudioData())
                            : poem.getAudioData();
                    return new PoemAudioData(audioBytes, poem.getAudioContentType(), poem.getAudioFilename());
                });
    }

    @Transactional
    public boolean deletePoemAudio(Long poemId) {
        Optional<Poem> existingPoem = poemRepository.findById(poemId);
        if (existingPoem.isEmpty()) {
            return false;
        }

        Poem poem = existingPoem.get();
        poem.setAudioData(null);
        poem.setAudioCompression(null);
        poem.setAudioContentType(null);
        poem.setAudioFilename(null);
        poem.setAudioOriginalSize(null);
        poem.setAudioCompressedSize(null);
        poemRepository.save(poem);
        return true;
    }

    private PoemResponse mapToResponse(Poem poem) {
        return PoemResponse.builder()
                .id(poem.getId())
                .title(poem.getTitle())
                .content(poem.getContent())
                .author(poem.getAuthor())
                .sede(poem.getSede())
                .hasAudio(poem.getAudioData() != null && poem.getAudioData().length > 0)
                .audioContentType(poem.getAudioContentType())
                .audioFilename(poem.getAudioFilename())
                .audioOriginalSize(poem.getAudioOriginalSize())
                .audioCompressedSize(poem.getAudioCompressedSize())
                .createdAt(poem.getCreatedAt())
                .updatedAt(poem.getUpdatedAt())
                .build();
    }

    private byte[] getFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo leer el archivo de audio", exception);
        }
    }

    public record PoemAudioData(byte[] bytes, String contentType, String fileName) {
    }
}
