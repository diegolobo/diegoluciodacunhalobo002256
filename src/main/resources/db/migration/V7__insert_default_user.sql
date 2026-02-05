-- Senha: admin123 (hash BCrypt cost=10)
INSERT INTO app_user (username, password, role, active) VALUES
    ('admin', '$2a$10$rDkPvvAFV6kXTpTEOqcFOe3kPpHxNvAIe6x/FPK3L.G/S3sA7CBmO', 'admin', TRUE);
