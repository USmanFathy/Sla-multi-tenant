package com.tenantFlow.tenant.repository;

import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository
        extends JpaRepository<Tenant, UUID> , JpaSpecificationExecutor<Tenant> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Tenant t WHERE t.id = :id")
    Optional<Tenant> findByIdForUpdate(@Param("id") UUID id);
    Optional<Tenant> findBySlug(String slug);
    List<Tenant> findAllByStatusInAndDatabaseNameIsNotNull(
            Collection<TenantStatus> statuses,
            Pageable pageable
    );

    List<Tenant> findAllByStatusInAndDatabaseNameIsNotNullAndIdGreaterThan(
            Collection<TenantStatus> statuses,
            UUID lastId,
            Pageable pageable
    );
}