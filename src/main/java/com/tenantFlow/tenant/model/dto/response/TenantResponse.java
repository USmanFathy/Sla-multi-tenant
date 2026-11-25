package com.tenantFlow.tenant.model.dto.response;


import com.tenantFlow.tenant.model.enums.TenantStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
public class TenantResponse {

    private UUID id;
    private String name;
    private String slug;
    private String email;
    private String code;
    private TenantStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}