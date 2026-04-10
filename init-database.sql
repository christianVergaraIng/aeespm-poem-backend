-- Script para ejecutar MANUALMENTE en Neon para crear las tablas
-- Ejecuta este script en la consola SQL de Neon

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id         BIGSERIAL    PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Tabla de poemas
CREATE TABLE IF NOT EXISTS poems (
    id         BIGSERIAL    PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    author     VARCHAR(100) NOT NULL,
    sede       VARCHAR(50)  NOT NULL,
    audio_data BYTEA,
    audio_content_type VARCHAR(100),
    audio_filename VARCHAR(255),
    audio_compression VARCHAR(20),
    audio_original_size INTEGER,
    audio_compressed_size INTEGER,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

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

-- Índices
CREATE INDEX IF NOT EXISTS idx_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_created_at ON poems(created_at);
CREATE INDEX IF NOT EXISTS idx_comment_poem_id ON comments(poem_id);
CREATE INDEX IF NOT EXISTS idx_comment_created_at ON comments(created_at);

-- Usuario administrador por defecto
-- Username: admin
-- Password: admin123 (encriptado con BCrypt)
INSERT INTO users (username, password, email, created_at, updated_at)
VALUES (
    'admin',
    '$2a$10$vYpk12hNw29iWxB3ebGvKeBbFUfWJjCSVuU3CI8SiG6wXrhJHqjCa',
    'admin@aeespm.com',
    NOW(),
    NOW()
)
ON CONFLICT (username) DO NOTHING;

-- Tabla de control de Flyway (para que no intente reejecutar migraciones)
CREATE TABLE IF NOT EXISTS flyway_schema_history (
    installed_rank INT NOT NULL PRIMARY KEY,
    version VARCHAR(50),
    description VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    script VARCHAR(1000) NOT NULL,
    checksum INT,
    installed_by VARCHAR(100) NOT NULL,
    installed_on TIMESTAMP NOT NULL DEFAULT NOW(),
    execution_time INT NOT NULL,
    success BOOLEAN NOT NULL
);

-- Registrar las migraciones como ya ejecutadas
INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success)
VALUES 
    (1, '1', 'Initial schema', 'SQL', 'V1__Initial_schema.sql', NULL, 'manual', NOW(), 0, true),
    (2, '2', 'Insert admin user', 'SQL', 'V2__Insert_admin_user.sql', NULL, 'manual', NOW(), 0, true),
    (3, '3', 'Update admin password', 'SQL', 'V3__Update_admin_password.sql', NULL, 'manual', NOW(), 0, true),
    (4, '4', 'Create comments table', 'SQL', 'V4__Create_comments_table.sql', NULL, 'manual', NOW(), 0, true)
ON CONFLICT (installed_rank) DO NOTHING;
