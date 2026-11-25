package com.tenantFlow.tenant.model.dto.request;

import com.tenantFlow.tenant.model.enums.TenantStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantFilterRequest {

    private TenantStatus status;

    private String name;

    private String code;
}