package com.tenantFlow.tenant.controller;
import com.tenantFlow.pagination.PaginationResponse;
import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.dto.request.TenantFilterRequest;
import com.tenantFlow.tenant.model.dto.response.TenantDetailsResponse;
import com.tenantFlow.tenant.model.dto.response.TenantResponse;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    public ResponseEntity<PaginationResponse<TenantResponse>> getTenants(
            @ModelAttribute TenantFilterRequest request,
            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                tenantService.getTenants(request, pageable)
        );
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @ModelAttribute CreateTenantRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tenantService.createTenant(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDetailsResponse> getTenantById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                tenantService.getTenantById(id)
        );
    }
    @PostMapping("/{id}/approve")
    public ResponseEntity<TenantResponse> approveTenant(
            @PathVariable UUID id
    ) {
        return ResponseEntity.accepted()
                .body(tenantService.approveTenant(id));
    }
}