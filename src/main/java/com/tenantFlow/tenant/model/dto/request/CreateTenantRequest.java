package com.tenantFlow.tenant.model.dto.request;

import com.tenantFlow.tenant.model.validation.annotation.ValidSlug;
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
    private String slug;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 254)
    private String email;
}