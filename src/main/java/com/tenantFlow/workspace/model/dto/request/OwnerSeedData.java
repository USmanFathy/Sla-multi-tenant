package com.tenantFlow.workspace.model.dto.request;

import java.util.UUID;

public record OwnerSeedData(

        UUID registrationId,

        String name,

        String username,

        String email

) {
}