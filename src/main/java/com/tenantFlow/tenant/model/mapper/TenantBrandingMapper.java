package com.tenantFlow.tenant.model.mapper;

import com.tenantFlow.tenant.model.dto.request.CreateTenantBrandingRequest;
import com.tenantFlow.tenant.model.dto.response.TenantBrandingResponse;
import com.tenantFlow.tenant.model.entity.TenantBranding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantBrandingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "logoKey", ignore = true)
    @Mapping(target = "faviconKey", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TenantBranding toEntity(CreateTenantBrandingRequest request);

    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "faviconUrl", ignore = true)
    TenantBrandingResponse toResponse(
            TenantBranding branding
    );
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "faviconUrl", ignore = true)
    List<TenantBrandingResponse> toResponseList(
            List<TenantBranding> brandings
    );
}