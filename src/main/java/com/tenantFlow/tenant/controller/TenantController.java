package com.tenantFlow.tenant.controller;
import com.tenantFlow.pagination.PaginationResponse;
import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.dto.request.TenantFilterRequest;
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
    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody CreateTenantRequest request
    ) {

        TenantResponse response =
                tenantService.createTenant(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<TenantResponse> detailTenant(@PathVariable UUID id){
        return ResponseEntity.ok(
                tenantService.getTenantById(id)
        );
    }
}