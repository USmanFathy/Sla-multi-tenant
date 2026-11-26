package com.tenantFlow.tenant.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantOwnerResponse {

    private String name;

    private String username;

    private String email;
}