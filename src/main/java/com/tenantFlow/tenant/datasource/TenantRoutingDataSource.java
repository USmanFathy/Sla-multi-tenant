package com.tenantFlow.tenant.datasource;

import com.tenantFlow.tenant.context.TenantContext;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class TenantRoutingDataSource
        extends AbstractRoutingDataSource {

    private final String baseUrl;
    private final String username;
    private final String password;

    public TenantRoutingDataSource(
            String baseUrl,
            String username,
            String password
    ) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;

        setTargetDataSources(Map.of());
        setLenientFallback(false);
    }

    @Override
    protected Object determineCurrentLookupKey() {

        return TenantContext.get();
    }

    @Override
    protected DataSource determineTargetDataSource() {

        String databaseName =
                (String) determineCurrentLookupKey();

        if (databaseName == null ||
                !databaseName.matches("^[a-zA-Z0-9_]{1,64}$")) {

            throw new IllegalStateException(
                    "Invalid or missing tenant context"
            );
        }

        return new DriverManagerDataSource(
                baseUrl + databaseName,
                username,
                password
        );
    }
}