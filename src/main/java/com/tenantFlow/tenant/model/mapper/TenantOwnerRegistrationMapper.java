package com.tenantFlow.tenant.model.mapper;

import com.tenantFlow.tenant.model.dto.request.CreateTenantOwnerRequest;
import com.tenantFlow.tenant.model.entity.TenantOwnerRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantOwnerRegistrationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TenantOwnerRegistration toEntity(
            CreateTenantOwnerRequest request
    );
}
