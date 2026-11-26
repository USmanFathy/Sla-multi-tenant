package com.tenantFlow.tenant.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTenantOwnerRequest {

    @NotBlank(message = "Owner name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Invalid username format"
    )
    private String username;

    @NotBlank(message = "Owner email is required")
    @Email(message = "Invalid owner email")
    @Size(max = 254)
    private String email;
}