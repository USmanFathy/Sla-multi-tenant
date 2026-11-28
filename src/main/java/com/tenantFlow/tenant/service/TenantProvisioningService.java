package com.tenantFlow.tenant.service;
import com.tenantFlow.tenant.context.TenantContext;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.entity.TenantOwnerRegistration;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.repository.TenantOwnerRegistrationRepository;
import com.tenantFlow.tenant.repository.TenantRepository;
import com.tenantFlow.workspace.model.dto.request.OwnerSeedData;
import com.tenantFlow.workspace.seeder.TenantOwnerSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantProvisioningService {

    private final DataSource dataSource;
    private final TenantRepository tenantRepository;
    private final TenantMigrationService migrationService;
    private final TenantOwnerSeeder tenantOwnerSeeder;
    private final TenantActivationService tenantActivationService;

    private final TenantOwnerRegistrationRepository
            tenantOwnerRegistrationRepository;
    public void provisionTenant(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found")
                );

        if (tenant.getStatus() != TenantStatus.PROVISIONING) {
            return;
        }

        createDatabase(tenant.getDatabaseName());
        migrationService.migrate(tenant.getDatabaseName());
        TenantOwnerRegistration registration =
                tenantOwnerRegistrationRepository
                        .findByTenant_Id(tenantId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Owner registration not found"
                                )
                        );

        OwnerSeedData ownerData = new OwnerSeedData(

                registration.getId(),

                registration.getName(),

                registration.getUsername(),

                registration.getEmail()
        );

        TenantContext.set(tenant.getDatabaseName());

        try {

            tenantOwnerSeeder.seed(ownerData);

        } finally {

            TenantContext.clear();
        }
        tenantActivationService.activate(tenantId);

        log.info("Tenant activated successfully: {}", tenantId);
    }

    public void createDatabase(String databaseName) {

        validateDatabaseName(databaseName);

        String sql = "CREATE DATABASE IF NOT EXISTS `"
                + databaseName + "`";

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {

            statement.execute(sql);

        } catch (SQLException e) {

            throw new IllegalStateException(
                    "Failed to create tenant database", e
            );
        }
    }
    private void validateDatabaseName(String databaseName) {

        if (databaseName == null ||
                !databaseName.matches("^[a-zA-Z0-9_]{1,64}$")) {

            throw new IllegalArgumentException(
                    "Invalid database name"
            );
        }
    }
}