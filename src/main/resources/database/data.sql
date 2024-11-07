-- Inserta una institución
INSERT INTO institutions (address, email_contact, logo, name, phone_contact)
VALUES ('1234 Elm St', 'contact@institution1.com', 'https://placehold.co/200x200', 'University of Code', '555-1234'),
       ('5678 Oak St', 'info@institution2.org', 'https://placehold.co/200x200', 'University of Pemo', '555-5678'),
       ('9101 Pine St', 'support@institution3.edu', 'https://placehold.co/200x200', 'Institution 1', '555-9101'),
       ('1121 Maple St', 'help@institution4.gov', 'https://placehold.co/200x200', 'Institution 2', '555-1121'),
       ('3141 Birch St', 'admin@institution5.net', 'https://placehold.co/200x200', 'Institution 3', '555-3141'),
       ('5161 Cedar St', 'service@institution6.co', 'https://placehold.co/200x200', 'Institution 4', '555-5161'),
       ('7181 Walnut St', 'contact@institution7.edu', 'https://placehold.co/200x200', 'Institution 5', '555-7181'),
       ('9201 Elm St', 'admin@institution8.org', 'https://placehold.co/200x200', 'Institution 6', '555-9201'),
       ('1221 Oak St', 'info@institution9.com', 'https://placehold.co/200x200', 'Institution 7', '555-1221'),
       ('3241 Pine St', 'support@institution10.net', 'https://placehold.co/200x200', 'Institution 8', '555-3241');

-- Inserta un rol
INSERT INTO roles (name)
VALUES ('capturista'),
       ('admin'),
       ('superadmin');

-- Inserta un estado
INSERT INTO statuses (name)
VALUES ('activo'),
       ('inactivo'),
       ('suspendido');

-- Inserta un usuario en user_accounts
INSERT INTO user_accounts (email, name, password, fk_institution, fk_rol, fk_status)
VALUES ('johndoe@example.com', 'John Doe', 'securepassword123', 1, 1, 1),
       ('smith@example.com', 'Smith', 'password123', 2, 1, 1),
       ('jhonson@example.com', 'Jhonson', 'password123', 3, 1, 1),
       ('someguy@example', 'Some Guy', 'password123', 4, 1, 1),
       ('alice@example', 'Alice', 'password123', 5, 1, 1),
       ('bob@example', 'Bob', 'password123', 6, 1, 1),
       ('charlie@example', 'Charlie', 'password123', 7, 1, 1),
       ('david@example', 'David', 'password123', 8, 1, 1),
       ('eve@example', 'Eve', 'password123', 9, 1, 1),
       ('frank@example', 'Frank', 'password123', 10, 1, 1);

-- Inserta un usuario en users_info
INSERT INTO users_info (is_in_card, is_in_qr, options, tag, type)
VALUES (true, false, '{"canEdit": true}', 'address', 'String'),
       (true, true, '{"canEdit": true}', 'email', 'String'),
       (true, false, '{"canEdit": true}', 'name', 'String'),
       (true, true, '{"canEdit": true}', 'phone', 'int'),
       (false,false, '{"canEdit": true}', 'curp', 'String'),
       (false,true, '{"canEdit": true}', 'rfc', 'String'),
       (false,false, '{"canEdit": true}', 'birthdate', 'Date'),
       (true, true, '{"canEdit": true}','role','String');