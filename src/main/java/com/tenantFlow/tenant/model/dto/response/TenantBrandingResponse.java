package com.tenantFlow.tenant.model.dto.response;

import com.tenantFlow.tenant.model.enums.BrandingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantBrandingResponse {

    private Integer version;

    private BrandingStatus status;

    private String primaryColor;

    private String secondaryColor;

    private String logoUrl;

    private String faviconUrl;
}