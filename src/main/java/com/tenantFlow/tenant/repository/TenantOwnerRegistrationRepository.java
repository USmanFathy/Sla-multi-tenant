package com.tenantFlow.tenant.repository;

import com.tenantFlow.tenant.model.entity.TenantOwnerRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantOwnerRegistrationRepository  extends JpaRepository<TenantOwnerRegistration, UUID> {
    Optional<TenantOwnerRegistration> findByTenant_Id(
            UUID tenantId
    );
}
