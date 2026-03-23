-- V3__Update_admin_password.sql
-- Update the admin user's password to be a BCrypt hash of 'admin123'
UPDATE users SET password = '$2a$10$vYpk12hNw29iWxB3ebGvKeBbFUfWJjCSVuU3CI8SiG6wXrhJHqjCa' WHERE username = 'admin';
