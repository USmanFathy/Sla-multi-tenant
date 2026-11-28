package com.tenantFlow.tenant.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;

@Component
@Slf4j
public class TenantDataSourceManager {

    private final Map<String, TenantPoolEntry> dataSources =
            new ConcurrentHashMap<>();

    @Value("${tenant.datasource.base-url}")
    private String baseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public Connection getConnection(String databaseName)
            throws SQLException {

        if (databaseName == null ||
                !databaseName.matches("^[a-zA-Z0-9_]{1,64}$")) {

            throw new IllegalArgumentException(
                    "Invalid tenant database name"
            );
        }

        TenantPoolEntry entry = getOrCreateEntry(databaseName);

        if (!entry.acquire()) {

            throw new SQLTransientConnectionException(
                    "Tenant pool is closing"
            );
        }

        Connection connection;

        try {
            connection = entry.getDataSource().getConnection();

        } catch (SQLException | RuntimeException e) {

            entry.release();

            throw e;
        }

        return TenantConnectionProxy.wrap(connection, entry);
    }
    private TenantPoolEntry getOrCreateEntry(String databaseName) {

        return dataSources.computeIfAbsent(
                databaseName,
                name -> new TenantPoolEntry(
                        createDataSource(name)
                )
        );
    }
    private HikariDataSource createDataSource(String databaseName) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(baseUrl + databaseName);
        config.setUsername(username);
        config.setPassword(password);

        config.setPoolName("pool-" + databaseName);

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(0);

        config.setIdleTimeout(300_000);
        config.setConnectionTimeout(30_000);
        config.setMaxLifetime(1_800_000);

        log.info("Creating connection pool for: {}", databaseName);

        return new HikariDataSource(config);
    }
    @PreDestroy
    public void shutdown() {

        log.info("Shutting down tenant connection pools");

        dataSources.forEach((databaseName, entry) -> {

            entry.getDataSource().close();

            log.info("Closed pool for: {}", databaseName);
        });

        dataSources.clear();
    }
}