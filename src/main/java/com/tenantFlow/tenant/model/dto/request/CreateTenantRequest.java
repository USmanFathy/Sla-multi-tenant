package com.tenantFlow.tenant.model.dto.request;

import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.validation.annotation.ValidSlug;
import com.tenantFlow.validation.annotation.Unique;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTenantRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Slug is required")
    @ValidSlug
    @Unique(
            entity = Tenant.class,
            field = "slug",
            message = "Slug already exists"
    )
    private String slug;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 254)
    private String email;
    @Valid
    @NotNull(message = "Owner information is required")
    private CreateTenantOwnerRequest owner;
    @Valid
    @NotNull(message = "branding information is required")
    private CreateTenantBrandingRequest branding;
}