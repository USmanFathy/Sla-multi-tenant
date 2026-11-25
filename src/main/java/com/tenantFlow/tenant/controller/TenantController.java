package com.tenantFlow.tenant.controller;
import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping("/count")
    public long countTenants() {
        return tenantService.countTenants();
    }
    @PostMapping
    public Tenant createTenant(
            @Valid @RequestBody CreateTenantRequest request
    ) {
        return tenantService.createTenant(request);
    }
}