package com.tenantFlow.tenant.model.mapper;

import com.tenantFlow.tenant.model.dto.request.CreateTenantBrandingRequest;
import com.tenantFlow.tenant.model.entity.TenantBranding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantBrandingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "logoKey", ignore = true)
    @Mapping(target = "faviconKey", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TenantBranding toEntity(CreateTenantBrandingRequest request);
}