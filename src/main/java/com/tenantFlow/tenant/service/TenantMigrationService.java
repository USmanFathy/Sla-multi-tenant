package com.tenantFlow.tenant.service;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
@Service
@Slf4j
public class TenantMigrationService {

    @Value("${tenant.datasource.base-url}")
    private String baseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public void migrate(String databaseName) {

        if (databaseName == null ||
                !databaseName.matches("^[a-zA-Z0-9_]{1,64}$")) {
            throw new IllegalArgumentException(
                    "Invalid database name"
            );
        }

        Flyway flyway = Flyway.configure()
                .dataSource(
                        baseUrl + databaseName,
                        username,
                        password
                )
                .locations("classpath:db/migration/tenant")
                .load();

        MigrateResult result = flyway.migrate();

        log.info(
                "Database: {}, migrations executed: {}",
                databaseName,
                result.migrationsExecuted
        );
    }
}