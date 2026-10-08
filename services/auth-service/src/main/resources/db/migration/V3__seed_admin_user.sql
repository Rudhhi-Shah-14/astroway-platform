-- Seed Admin User (Password: AdminSecret123!)
-- Hash generated via BCrypt
INSERT INTO users (username, email, password, enabled)
VALUES ('admin_astro', 'admin@astroway.com', '$2a$10$eE0o9X1F3gG/V9o/R/8P3.5U6t2yL4x8w1z9a0b1c2d3e4f5g6h7i', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin_astro' AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;