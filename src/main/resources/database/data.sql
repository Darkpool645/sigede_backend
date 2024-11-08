USE sigede;

-- 1. Inserta una institución en 'institutions'
INSERT INTO institutions (address, email_contact, logo, name, phone_contact)
VALUES
    ('1234 Elm St', 'contact@institution1.com', 'https://placehold.co/200x200', 'University of Code', '555-1234'),
    ('5678 Oak St', 'info@institution2.org', 'https://placehold.co/200x200', 'University of Pemo', '555-5678'),
    ('9101 Pine St', 'support@institution3.edu', 'https://placehold.co/200x200', 'Institution 1', '555-9101'),
    ('1121 Maple St', 'help@institution4.gov', 'https://placehold.co/200x200', 'Institution 2', '555-1121'),
    ('3141 Birch St', 'admin@institution5.net', 'https://placehold.co/200x200', 'Institution 3', '555-3141'),
    ('5161 Cedar St', 'service@institution6.co', 'https://placehold.co/200x200', 'Institution 4', '555-5161'),
    ('7181 Walnut St', 'contact@institution7.edu', 'https://placehold.co/200x200', 'Institution 5', '555-7181'),
    ('9201 Elm St', 'admin@institution8.org', 'https://placehold.co/200x200', 'Institution 6', '555-9201');

-- 2. Inserta roles en 'roles'
INSERT INTO roles (name)
VALUES
    ('capturista'),
    ('admin'),
    ('superadmin');

-- 3. Inserta estados en 'statuses'
INSERT INTO statuses (name)
VALUES
    ('activo'),
    ('inactivo'),
    ('suspendido');

-- 4. Inserta usuarios en 'user_accounts' (después de 'institutions', 'roles', y 'statuses')
INSERT INTO user_accounts (email, name, password, fk_institution, fk_rol, fk_status)
VALUES
    ('johndoe@example.com', 'John Doe', 'securepassword123', 1, 1, 1),
    ('smith@example.com', 'Smith', 'password123', 2, 1, 1),
    ('jhonson@example.com', 'Jhonson', 'password123', 3, 1, 1),
    ('someguy@example', 'Some Guy', 'password123', 4, 1, 1),
    ('alice@example', 'Alice', 'password123', 5, 1, 1),
    ('bob@example', 'Bob', 'password123', 6, 1, 1),
    ('charlie@example', 'Charlie', 'password123', 7, 1, 1),
    ('david@example', 'David', 'password123', 8, 1, 1);

-- 5. Inserta información en 'users_info'
INSERT INTO users_info (is_in_card, is_in_qr, options, tag, type)
VALUES
    (true, false, '{"canEdit": true}', 'address', 'String'),
    (true, true, '{"canEdit": true}', 'email', 'String'),
    (true, false, '{"canEdit": true}', 'name', 'String'),
    (true, true, '{"canEdit": true}', 'phone', 'int'),
    (false,false, '{"canEdit": true}', 'curp', 'String'),
    (false,true, '{"canEdit": true}', 'rfc', 'String'),
    (false,false, '{"canEdit": true}', 'birthdate', 'Date'),
    (true, true, '{"canEdit": true}','role','String');

-- 6. Inserta perfiles en 'capturist_profiles'
INSERT INTO capturist_profiles (fk_profile)
VALUES
    (1), (2), (3), (4), (5), (6), (7), (8);

-- 7. Inserta credenciales en 'credentials'
INSERT INTO credentials (expiration_date, issue_date, user_photo, fullname, fk_institution, fk_user_account)
VALUES
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'John Doe', 1, 1),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'Smith', 2, 2),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'Jhonson', 3, 3),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'Some Guy', 4, 4),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'Alice', 5, 5),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'Bob', 6, 6),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'Charlie', 7, 7),
    ('2025-12-31 23:59:59', '2021-01-01 00:00:00', 'https://placehold.co/200x200', 'David', 8, 8);

-- 8. Inserta campos de credencial en 'credential_fields'
INSERT INTO credential_fields (value, fk_credential, fk_user_info)
VALUES
    ('1234 Elm St', 1, 1),
    ('example@example.com', 1, 2),
    ('John Doe', 1, 3),
    ('555-1234', 1, 4),
    ('GAAJ920708HDFRRR07', 1, 5),
    ('GAAJ920708F54', 1, 6),
    ('1990-01-01', 1, 7),
    ('capturista', 1, 8);

-- 9. Inserta campos de capturista en 'institution_capturist_fields'
INSERT INTO institution_capturist_fields (is_required, fk_institution, fk_user_info)
VALUES
    (true, 1, 1),
    (true, 2, 2),
    (true, 3, 3),
    (true, 4, 4),
    (true, 5, 5),
    (true, 6, 6),
    (true, 7, 7),
    (true, 8, 8);

-- 10. Inserta códigos de verificación en 'verification_codes'
INSERT INTO verification_codes (created_at, expiration, verification_code, fk_user_account)
VALUES
    (NOW(), '2024-12-31 23:59:59', 'ABC123XYZ', 1),
    (NOW(), '2024-12-31 23:59:59', 'ABC133XYZ', 2),
    (NOW(), '2024-12-31 23:59:59', 'ABC143XYZ', 3),
    (NOW(), '2024-12-31 23:59:59', 'ABC153XYZ', 4),
    (NOW(), '2024-12-31 23:59:59', 'ABC163XYZ', 5),
    (NOW(), '2024-12-31 23:59:59', 'ABC173XYZ', 6),
    (NOW(), '2024-12-31 23:59:59', 'ABC183XYZ', 7),
    (NOW(), '2024-12-31 23:59:59', 'ABC193XYZ', 8);