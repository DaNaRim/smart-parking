INSERT INTO role (role_name)
VALUES ('USER'),
       ('ADMIN'),
       ('SUPER_ADMIN'),
       ('GUARD')
    ON CONFLICT DO NOTHING;
