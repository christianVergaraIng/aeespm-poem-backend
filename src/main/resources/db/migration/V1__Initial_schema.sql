-- V1__Initial_schema.sql
-- Esquema inicial convertido de MySQL a PostgreSQL

-- Tabla de usuarios
CREATE TABLE users (
                       id         BIGSERIAL    PRIMARY KEY,
                       username   VARCHAR(50)  NOT NULL UNIQUE,
                       password   VARCHAR(255) NOT NULL,
                       email      VARCHAR(100),
                       created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Tabla de poemas
CREATE TABLE poems (
                       id         BIGSERIAL    PRIMARY KEY,
                       title      VARCHAR(255) NOT NULL,
                       content    TEXT         NOT NULL,
                       author     VARCHAR(100) NOT NULL,
                       sede       VARCHAR(50)  NOT NULL,
                       created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Índices
CREATE INDEX idx_username   ON users(username);
CREATE INDEX idx_created_at ON poems(created_at);