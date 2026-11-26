package com.tenantFlow.tenant.listener;

import com.tenantFlow.tenant.event.TenantApprovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class TenantApprovedEventListener {

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(TenantApprovedEvent event) {

        log.info(
                "Starting provisioning for tenant: {}",
                event.tenantId()
        );
    }
}