package com.tenantFlow.tenant.listener;

import com.tenantFlow.tenant.event.TenantApprovedEvent;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.service.TenantMigrationService;
import com.tenantFlow.tenant.service.TenantProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TenantApprovedEventListener {
    private final TenantProvisioningService provisioningService;
    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(TenantApprovedEvent event) {

        provisioningService.provisionTenant(
                event.tenantId()
        );

    }
}