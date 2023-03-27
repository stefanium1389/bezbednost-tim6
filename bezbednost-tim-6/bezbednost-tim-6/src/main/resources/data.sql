INSERT INTO users (activated, address, blocked, email, name, profile_picture, surname, password, telephone_number, role_id)
VALUES (true, 'Narodnog fronta 5', false, 'andrijinkristina@gmail.com', 'Kristina', 'profile_picture5', 'Andrijin', '$2a$10$iYCFgw.vl7KlYVikqSbLtOyAR5lNEKalF29dfYfGn7SRiE8pbskeu', '6521545154', 1);

INSERT INTO roles (name) VALUES ("admin");
INSERT INTO roles (name) VALUES ("user");