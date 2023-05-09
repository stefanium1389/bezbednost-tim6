INSERT INTO users (email, password, name, surname, telephone_number, role_id, activated)
VALUES ('andrijinkristina@gmail.com', '$2a$10$iYCFgw.vl7KlYVikqSbLtOyAR5lNEKalF29dfYfGn7SRiE8pbskeu', 'Kristina','Andrijin', '6521545154', 1, true);

INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

--insert into certificates (serial_number, certificate_type, issuer, signature_algorithm, status, valid_from, valid_to, user_id)
--values (1680527310325, "ROOT", 1, "SHA256withRSA", "VALID", "2023-04-03 14:59:27", "2027-05-12 14:59:27", 1);
