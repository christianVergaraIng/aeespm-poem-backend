-- Script para agregar ÚNICAMENTE la tabla de comentarios
-- Ejecuta este script si ya tienes la base de datos creada y solo quieres añadir los comentarios

-- Tabla de comentarios
CREATE TABLE IF NOT EXISTS comments (
    id         BIGSERIAL    PRIMARY KEY,
    nickname   VARCHAR(100) NOT NULL,
    content    TEXT         NOT NULL,
    poem_id    BIGINT       NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_comment_poem FOREIGN KEY (poem_id) REFERENCES poems(id) ON DELETE CASCADE
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_comment_poem_id ON comments(poem_id);
CREATE INDEX IF NOT EXISTS idx_comment_created_at ON comments(created_at);

-- Registrar la migración en Flyway
INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
VALUES 
    (4, '4', 'Create comments table', 'SQL', 'V4__Create_comments_table.sql', NULL, 'manual', NOW(), 0, true)
ON CONFLICT (installed_rank) DO NOTHING;
