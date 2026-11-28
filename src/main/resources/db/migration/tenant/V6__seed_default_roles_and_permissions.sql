INSERT INTO permissions (id, name, description)
VALUES
    (UUID_TO_BIN(UUID()), 'users.view', 'View users'),
    (UUID_TO_BIN(UUID()), 'users.create', 'Create users'),
    (UUID_TO_BIN(UUID()), 'users.update', 'Update users'),
    (UUID_TO_BIN(UUID()), 'users.suspend', 'Suspend users'),

    (UUID_TO_BIN(UUID()), 'roles.view', 'View roles'),
    (UUID_TO_BIN(UUID()), 'roles.create', 'Create roles'),
    (UUID_TO_BIN(UUID()), 'roles.update', 'Update roles'),
    (UUID_TO_BIN(UUID()), 'roles.delete', 'Delete roles'),

    (UUID_TO_BIN(UUID()), 'permissions.view', 'View permissions');


INSERT INTO roles (id, name, description, is_system)
VALUES (
           UUID_TO_BIN(UUID()),
           'OWNER',
           'Tenant owner role',
           TRUE
       );


INSERT INTO role_permissions (role_id, permission_id)
SELECT roles.id, permissions.id
FROM roles
         CROSS JOIN permissions
WHERE roles.name = 'OWNER';