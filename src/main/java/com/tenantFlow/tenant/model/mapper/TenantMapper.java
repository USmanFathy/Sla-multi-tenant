package com.tenantFlow.tenant.model.mapper;

import com.tenantFlow.tenant.model.dto.request.CreateTenantRequest;
import com.tenantFlow.tenant.model.dto.response.TenantDetailsResponse;
import com.tenantFlow.tenant.model.dto.response.TenantResponse;
import com.tenantFlow.tenant.model.entity.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "databaseName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tenant toEntity(CreateTenantRequest request);

    TenantResponse toResponse(Tenant tenant);
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "brandings", ignore = true)
    TenantDetailsResponse toDetailsResponse(Tenant tenant);
}