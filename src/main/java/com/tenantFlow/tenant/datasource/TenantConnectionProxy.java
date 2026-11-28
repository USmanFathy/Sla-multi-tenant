package com.tenantFlow.tenant.datasource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TenantConnectionProxy {

    private TenantConnectionProxy() {
    }

    public static Connection wrap(
            Connection connection,
            TenantPoolEntry entry
    ) {

        AtomicBoolean released = new AtomicBoolean(false);

        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},

                (proxy, method, args) -> {

                    if (method.getName().equals("close")) {

                        if (released.compareAndSet(false, true)) {

                            try {
                                connection.close();
                            } finally {
                                entry.release();
                            }
                        }

                        return null;
                    }

                    try {
                        return method.invoke(connection, args);

                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }
        );
    }
}