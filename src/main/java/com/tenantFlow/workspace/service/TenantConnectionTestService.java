package com.tenantFlow.workspace.service;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
@Service
public class TenantConnectionTestService {

    private final DataSource dataSource;

    public TenantConnectionTestService(
            @Qualifier("tenantDataSource")
            DataSource dataSource
    ) {
        this.dataSource = dataSource;
    }

    public String getCurrentDatabase() {

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(
                        "SELECT DATABASE()"
                )
        ) {

            if (result.next()) {
                return result.getString(1);
            }

            throw new IllegalStateException(
                    "No database selected"
            );

        } catch (SQLException e) {

            throw new IllegalStateException(
                    "Failed to resolve current database", e
            );
        }
    }
}