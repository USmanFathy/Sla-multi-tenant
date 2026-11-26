package com.tenantFlow.tenant.event;

import java.util.UUID;

public record TenantApprovedEvent(UUID tenantId) {
}
