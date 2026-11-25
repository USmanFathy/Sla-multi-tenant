package com.tenantFlow.tenant.service;

import org.springframework.stereotype.Service;

import com.tenantFlow.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    public long countTenants() {
        return tenantRepository.count();
    }
}