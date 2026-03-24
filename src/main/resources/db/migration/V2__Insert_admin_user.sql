-- V2__Insert_admin_user.sql
-- Usuario administrador por defecto
-- Username: admin
-- Password: admin123 (encriptado con BCrypt strength 10)
INSERT INTO users (username, password, email, created_at, updated_at)
VALUES (
           'admin',
           '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
           'admin@aeespm.com',
           NOW(),
           NOW()
       );