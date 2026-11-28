CREATE TABLE permissions (

                             id BINARY(16) NOT NULL,

                             name VARCHAR(100) NOT NULL,

                             description VARCHAR(255),

                             PRIMARY KEY (id),

                             CONSTRAINT uk_permissions_name UNIQUE (name)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;