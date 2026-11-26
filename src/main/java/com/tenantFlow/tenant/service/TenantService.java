package com.tenantFlow.tenant.service;

import com.tenantFlow.pagination.PaginationResponse;
import com.tenantFlow.pagination.SortValidator;
import com.tenantFlow.specification.FilterCriteria;
import com.tenantFlow.specification.FilterOperation;
import com.tenantFlow.specification.GenericSpecification;
import com.tenantFlow.tenant.event.TenantApprovedEvent;
import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.dto.request.TenantFilterRequest;
import com.tenantFlow.tenant.model.dto.response.TenantDetailsResponse;
import com.tenantFlow.tenant.model.dto.response.TenantResponse;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.entity.TenantOwnerRegistration;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.model.mapper.TenantMapper;
import com.tenantFlow.tenant.model.mapper.TenantOwnerRegistrationMapper;
import com.tenantFlow.tenant.repository.TenantOwnerRegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tenantFlow.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TenantService {
    @Value("${tenant.prefix}")
    private String prefix;
    @Value("${tenant.suffix}")
    private String suffix;
    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of(
                    "name",
                    "status",
                    "createdAt",
                    "updatedAt",
                    "code",
                    "slug"
            );
    private final TenantOwnerRegistrationRepository ownerRepository;
    private final TenantBrandingService tenantBrandingService;
    private final TenantOwnerRegistrationMapper ownerMapper;
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private static final SecureRandom random = new SecureRandom();
    private final ApplicationEventPublisher eventPublisher;
    private String generateTenantCode() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
    public long countTenants() {
        return tenantRepository.count();
    }
    @Transactional
    public TenantResponse createTenant(CreateTenantRequest request) {

        Tenant tenant = tenantMapper.toEntity(request);

        tenant.setStatus(TenantStatus.PENDING);
        tenant.setCode(generateTenantCode());

        Tenant savedTenant = tenantRepository.save(tenant);

        TenantOwnerRegistration owner =
                ownerMapper.toEntity(request.getOwner());

        owner.setTenant(savedTenant);

        ownerRepository.save(owner);
        tenantBrandingService.createInitialBranding(
                savedTenant,
                request.getBranding()
        );
        return tenantMapper.toResponse(savedTenant);
    }
    public Page<TenantResponse> getTenants(Pageable pageable) {

        return tenantRepository.findAll(pageable)
                .map(tenantMapper::toResponse);
    }
    public PaginationResponse<TenantResponse> getTenants(
            TenantFilterRequest request,
            Pageable pageable
    ) {
        SortValidator.validate(pageable, ALLOWED_SORT_FIELDS);
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

    @Transactional(readOnly = true)
    public TenantDetailsResponse getTenantById(UUID id) {

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tenant not found"
                ));

        TenantDetailsResponse response =
                tenantMapper.toDetailsResponse(tenant);
        TenantOwnerRegistration owner =
                ownerRepository.findByTenant_Id(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Tenant owner registration not found"
                        ));

        response.setOwner(ownerMapper.toResponse(owner));
        response.setBrandings(
                tenantBrandingService.getBrandings(id)
        );
        return response;
    }
    @Transactional
    public TenantResponse approveTenant(UUID id) {

        Tenant tenant = tenantRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tenant not found"
                ));

        if (tenant.getStatus() != TenantStatus.PENDING) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Tenant is not pending approval"
            );
        }

        tenant.setDatabaseName(
                generateDatabaseName(id)
        );

        tenant.setStatus(TenantStatus.PROVISIONING);
        eventPublisher.publishEvent(
                new TenantApprovedEvent(id)
        );
        return tenantMapper.toResponse(tenant);
    }
    private String generateDatabaseName(UUID tenantId) {
        return prefix
                + tenantId.toString().replace("-", "")
                + suffix;
    }
}