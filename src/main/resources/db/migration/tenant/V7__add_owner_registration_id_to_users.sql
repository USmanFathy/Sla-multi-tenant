ALTER TABLE users

    ADD COLUMN owner_registration_id BINARY(16) NULL,

    ADD CONSTRAINT uk_users_owner_registration
        UNIQUE (owner_registration_id);