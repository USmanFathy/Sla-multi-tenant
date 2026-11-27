package com.tenantFlow.tenant.command;

import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.repository.TenantRepository;
import com.tenantFlow.tenant.service.TenantMigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        name = "tenant.command",
        havingValue = "migrate"
)
public class TenantMigrationCommand implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final TenantMigrationService migrationService;

    private static final int BATCH_SIZE = 100;

    @Override
    public void run(String... args) {

        int succeeded = 0;
        int failed = 0;

        UUID lastId = null;

        Pageable pageable = PageRequest.of(
                0,
                BATCH_SIZE,
                Sort.by("id").ascending()
        );

        List<TenantStatus> statuses = List.of(
                TenantStatus.ACTIVE,
                TenantStatus.SUSPENDED
        );

        while (true) {

            List<Tenant> tenants;

            if (lastId == null) {

                tenants = tenantRepository
                        .findAllByStatusInAndDatabaseNameIsNotNull(
                                statuses,
                                pageable
                        );

            } else {

                tenants = tenantRepository
                        .findAllByStatusInAndDatabaseNameIsNotNullAndIdGreaterThan(
                                statuses,
                                lastId,
                                pageable
                        );
            }

            if (tenants.isEmpty()) {
                break;
            }

            for (Tenant tenant : tenants) {

                try {

                    migrationService.migrate(
                            tenant.getDatabaseName()
                    );

                    succeeded++;

                } catch (Exception e) {

                    failed++;

                    log.error(
                            "Migration failed for tenant: {}",
                            tenant.getId(),
                            e
                    );
                }
            }

            lastId = tenants.getLast().getId();
        }

        log.info(
                "Migration completed. Total: {}, Success: {}, Failed: {}",
                succeeded + failed,
                succeeded,
                failed
        );

        if (failed > 0) {
            throw new IllegalStateException(
                    "Migration failed for " + failed + " tenants"
            );
        }
    }
}