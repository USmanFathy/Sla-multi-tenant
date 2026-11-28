CREATE TABLE roles (

    id BINARY(16) NOT NULL,

    name VARCHAR(100) NOT NULL,

    description VARCHAR(255),

    is_system BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY (id),

    CONSTRAINT uk_roles_name UNIQUE (name)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;