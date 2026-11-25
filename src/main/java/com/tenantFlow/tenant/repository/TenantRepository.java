package com.tenantFlow.tenant.repository;

import com.tenantFlow.tenant.model.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository
        extends JpaRepository<Tenant, UUID> {

}