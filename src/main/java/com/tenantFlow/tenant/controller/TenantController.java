package com.tenantFlow.tenant.controller;
import com.tenantFlow.tenant.service.TenantService;
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
}