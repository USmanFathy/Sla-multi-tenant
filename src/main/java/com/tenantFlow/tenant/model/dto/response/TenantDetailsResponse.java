package com.tenantFlow.tenant.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TenantDetailsResponse extends TenantResponse {

    private TenantOwnerResponse owner;

    private List<TenantBrandingResponse> brandings;
}