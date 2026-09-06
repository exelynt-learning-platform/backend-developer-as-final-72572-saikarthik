-- Insert seed users
INSERT INTO users (username, email, password, role, created_at) VALUES 
('admin', 'admin@example.com', '$2a$12$H5.yFxZpXp0GKVM3LlBBYu5t5r9q.oWn2VvjBjfkWpZJ0H5q1uFsG', 'ADMIN', NOW()),
('user1', 'user1@example.com', '$2a$12$rKx7NyJ5qP5qZ4qR3dL5L.Q9O2N8K7J6H5G4F3E2D1C0B9A8.xxQFi', 'USER', NOW()),
('user2', 'user2@example.com', '$2a$12$rKx7NyJ5qP5qZ4qR3dL5L.Q9O2N8K7J6H5G4F3E2D1C0B9A8.xxQFi', 'USER', NOW());

-- Insert seed resources
INSERT IGNORE INTO resources (name, description, type, price, available, created_at, updated_at) VALUES 
('Conference Room A', 'Large conference room with 20 seats', 'Room', 100.00, true, NOW(), NOW()),
('Meeting Room B', 'Small meeting room with 8 seats', 'Room', 50.00, true, NOW(), NOW()),
('Projector', 'Full HD projector for presentations', 'Equipment', 25.00, true, NOW(), NOW()),
('Whiteboard', '6x4 feet whiteboard with markers', 'Equipment', 15.00, true, NOW(), NOW()),
('Company Vehicle', 'Toyota Corolla for business trips', 'Vehicle', 75.00, true, NOW(), NOW()),
('Laptop', 'Dell XPS 13 for presentations', 'Equipment', 30.00, true, NOW(), NOW());

-- Insert seed reservations
INSERT IGNORE INTO reservations (resource_id, user_id, start_time, end_time, price, status, created_at, updated_at) VALUES 
(1, 2, DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 100.00, 'CONFIRMED', NOW(), NOW()),
(3, 3, DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 25.00, 'PENDING', NOW(), NOW()),
(5, 2, DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 6 DAY), 75.00, 'CONFIRMED', NOW(), NOW());
