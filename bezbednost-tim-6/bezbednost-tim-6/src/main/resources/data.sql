INSERT INTO users (email, password, name, surname, telephone_number, role_id, activated, verify_with_mail)
VALUES ('andrijinkristina@gmail.com', '$2a$10$iYCFgw.vl7KlYVikqSbLtOyAR5lNEKalF29dfYfGn7SRiE8pbskeu', 'Kristina','Andrijin', '6521545154', 1, true, true);
INSERT INTO users (email, password, name, surname, telephone_number, role_id, activated, verify_with_mail)
VALUES ('stefanbogdjr@gmail.com', '$2a$10$iYCFgw.vl7KlYVikqSbLtOyAR5lNEKalF29dfYfGn7SRiE8pbskeu', 'Stefan','Bogdanovic', '644962814', 2, true, true);

INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

-- insert into certificates (serial_number, certificate_revocation_status, certificate_type, issuer, signature_algorithm, status, valid_from, valid_to, common_name,user_id, revocation_reason)
-- values (1684917705279,"GOOD", "ROOT", null, "SHA256withRSA", "VALID", "2023-05-24 10:41:45", "2024-05-26 10:41:45","lalala", 1, null);
