package com.tenantFlow.tenant.service;

import com.tenantFlow.pagination.PaginationResponse;
import com.tenantFlow.specification.FilterCriteria;
import com.tenantFlow.specification.FilterOperation;
import com.tenantFlow.specification.GenericSpecification;
import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.dto.request.TenantFilterRequest;
import com.tenantFlow.tenant.model.dto.response.TenantResponse;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.model.mapper.TenantMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tenantFlow.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
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
    public TenantResponse createTenant(CreateTenantRequest createTenantRequest){
        Tenant tenant = this.tenantMapper.toEntity(createTenantRequest);
        tenant.setStatus(TenantStatus.PENDING);
        tenant.setCode(generateTenantCode());
        Tenant savedTenant = this.tenantRepository.save(tenant);
        return this.tenantMapper.toResponse(savedTenant);
    }
    public Page<TenantResponse> getTenants(Pageable pageable) {

        return tenantRepository.findAll(pageable)
                .map(tenantMapper::toResponse);
    }
    public PaginationResponse<TenantResponse> getTenants(
            TenantFilterRequest request,
            Pageable pageable
    ) {

        List<FilterCriteria> filters = List.of(
                new FilterCriteria(
                        "status", request.getStatus(),
                        FilterOperation.EQUAL
                ),
                new FilterCriteria(
                        "name", request.getName(),
                        FilterOperation.LIKE
                ),
                new FilterCriteria(
                        "code", request.getCode(),
                        FilterOperation.EQUAL
                )
        );

        Page<TenantResponse> tenants =
                tenantRepository.findAll(
                        GenericSpecification.<Tenant>filters(filters),
                        pageable
                ).map(tenantMapper::toResponse);

        return PaginationResponse.from(tenants);
    }

    public TenantResponse getTenantById(UUID id) {

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tenant not found"
                ));

        return tenantMapper.toResponse(tenant);
    }
}