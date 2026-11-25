package com.tenantFlow.tenant.service;

import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.model.mapper.TenantMapper;
import org.springframework.stereotype.Service;

import com.tenantFlow.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private static final SecureRandom random = new SecureRandom();

    private String generateTenantCode() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
    public long countTenants() {
        return tenantRepository.count();
    }
    public Tenant createTenant(CreateTenantRequest createTenantRequest){
        Tenant tenant = this.tenantMapper.toEntity(createTenantRequest);
        tenant.setStatus(TenantStatus.PENDING);
        tenant.setCode(generateTenantCode());
        return this.tenantRepository.save(tenant);
    }
}