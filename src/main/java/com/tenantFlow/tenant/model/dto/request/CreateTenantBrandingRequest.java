package com.tenantFlow.tenant.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateTenantBrandingRequest {

    @NotBlank
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$")
    private String primaryColor;

    @NotBlank
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$")
    private String secondaryColor;

    private MultipartFile logo;

    private MultipartFile favicon;
}