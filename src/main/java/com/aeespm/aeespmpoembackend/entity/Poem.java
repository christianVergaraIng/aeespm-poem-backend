package com.aeespm.aeespmpoembackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "poems")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"comments", "audioData"})
@ToString(exclude = {"comments", "audioData"})
public class Poem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, length = 100)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sede sede;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "audio_data", columnDefinition = "BYTEA")
    private byte[] audioData;

    @Column(name = "audio_content_type", length = 100)
    private String audioContentType;

    @Column(name = "audio_filename", length = 255)
    private String audioFilename;

    @Column(name = "audio_compression", length = 20)
    private String audioCompression;

    @Column(name = "audio_original_size")
    private Integer audioOriginalSize;

    @Column(name = "audio_compressed_size")
    private Integer audioCompressedSize;

    @OneToMany(mappedBy = "poem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
