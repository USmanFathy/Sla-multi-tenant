package com.tenantFlow.tenant.repository;

import com.tenantFlow.tenant.model.entity.TenantBranding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TenantBrandingRepository
        extends JpaRepository<TenantBranding, UUID> {

    Page<TenantBranding> findByTenant_Id(
            UUID tenantId,
            Pageable pageable
    );
    List<TenantBranding> findAllByTenant_IdOrderByVersionDesc(
            UUID tenantId
    );
}