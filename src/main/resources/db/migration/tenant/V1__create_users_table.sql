CREATE TABLE users (

                       id BINARY(16) NOT NULL,

                       name VARCHAR(100) NOT NULL,

                       username VARCHAR(50) NOT NULL,

                       email VARCHAR(254) NOT NULL,

                       password VARCHAR(255) NULL,

                       avatar_key VARCHAR(255) NULL,

                       status VARCHAR(20) NOT NULL DEFAULT 'INVITED',

                       created_at DATETIME(6) NOT NULL
        DEFAULT CURRENT_TIMESTAMP(6),

                       updated_at DATETIME(6) NOT NULL
        DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),

                       PRIMARY KEY (id),

                       CONSTRAINT uk_users_username UNIQUE (username),

                       CONSTRAINT uk_users_email UNIQUE (email)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;