package com.tenantFlow.tenant.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TenantPoolEntry {

    @Getter
    private final HikariDataSource dataSource;

    private long lastAccessTime;

    private int activeOperations = 0;

    private boolean closed = false;

    public TenantPoolEntry(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public synchronized boolean acquire() {

        if (closed) {
            return false;
        }

        activeOperations++;
        lastAccessTime = System.currentTimeMillis();

        log.info("Pool acquired: {} | Active: {}",
                dataSource.getPoolName(),
                activeOperations);

        return true;
    }

    public synchronized void release() {

        if (activeOperations == 0) {
            throw new IllegalStateException(
                    "No active operations to release"
            );
        }

        activeOperations--;
        lastAccessTime = System.currentTimeMillis();

        log.info("Pool released: {} | Active: {}",
                dataSource.getPoolName(),
                activeOperations);
    }

    public synchronized boolean markClosedIfIdle(
            long idleTimeout
    ) {

        long idleTime =
                System.currentTimeMillis() - lastAccessTime;

        if (closed || activeOperations > 0
                || idleTime < idleTimeout) {

            return false;
        }

        closed = true;

        return true;
    }
}