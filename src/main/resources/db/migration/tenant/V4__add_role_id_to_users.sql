ALTER TABLE users

    ADD COLUMN role_id BINARY(16) NOT NULL,

    ADD INDEX idx_users_role_id (role_id),

    ADD CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE RESTRICT;