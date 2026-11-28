package com.tenantFlow.workspace.controller;
import com.tenantFlow.workspace.service.TenantConnectionTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Profile("dev")
public class TenantConnectionTestController {

    private final TenantConnectionTestService testService;

    @GetMapping("/database")
    public String getDatabase() {

        return testService.getCurrentDatabase();
    }
}