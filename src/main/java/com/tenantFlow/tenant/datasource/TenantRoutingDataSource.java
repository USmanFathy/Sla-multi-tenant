package com.tenantFlow.tenant.datasource;

import com.tenantFlow.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

@RequiredArgsConstructor
public class TenantRoutingDataSource
        extends AbstractDataSource {

    private final TenantDataSourceManager dataSourceManager;

    @Override
    public Connection getConnection() throws SQLException {

        String databaseName = TenantContext.get();

        return dataSourceManager.getConnection(databaseName);
    }

    @Override
    public Connection getConnection(
            String username,
            String password
    ) throws SQLException {

        throw new SQLFeatureNotSupportedException(
                "Custom credentials are not supported"
        );
    }
}