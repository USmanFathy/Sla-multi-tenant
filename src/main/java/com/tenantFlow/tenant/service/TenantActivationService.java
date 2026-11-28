package com.tenantFlow.tenant.service;

import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.repository.TenantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantActivationService {

    private final TenantRepository tenantRepository;

    @Transactional("transactionManager")
    public void activate(UUID tenantId) {

        Tenant tenant = tenantRepository
                .findByIdForUpdate(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Tenant not found"
                        )
                );

        if (tenant.getStatus() == TenantStatus.ACTIVE) {
            return;
        }

        if (tenant.getStatus() != TenantStatus.PROVISIONING) {

            throw new IllegalStateException(
                    "Tenant cannot be activated"
            );
        }

        tenant.setStatus(TenantStatus.ACTIVE);
    }
}