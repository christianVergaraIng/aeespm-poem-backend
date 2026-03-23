-- V2__Insert_admin_user.sql
-- Insert default admin user
-- Username: admin
-- Password: admin123 (encrypted with BCrypt)
INSERT INTO users (username, password, email, created_at, updated_at) 
VALUES ('admin', 'admin123', 'admin@aeespm.com', NOW(), NOW());
