package com.tenantFlow.tenant.context;

public final class TenantContext {

    private static final ThreadLocal<String> currentTenant =
            new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(String databaseName) {
        currentTenant.set(databaseName);
    }

    public static String get() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}