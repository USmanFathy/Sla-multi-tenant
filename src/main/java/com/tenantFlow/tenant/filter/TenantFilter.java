package com.tenantFlow.tenant.filter;

import com.tenantFlow.tenant.context.TenantContext;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.enums.TenantStatus;
import com.tenantFlow.tenant.repository.TenantRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    private final TenantRepository tenantRepository;

    @Value("${tenant.base-domain}")
    private String baseDomain;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String host = request.getServerName()
                    .toLowerCase(Locale.ROOT);

            // Central Request
            if (host.equalsIgnoreCase(baseDomain)) {

                filterChain.doFilter(request, response);

                return;
            }

            // Extract Tenant Slug
            String slug = extractSlug(host);

            if (slug == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid tenant domain"
                );

                return;
            }

            // Find Tenant
            Tenant tenant = tenantRepository
                    .findBySlug(slug)
                    .orElse(null);

            if (tenant == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Tenant not found"
                );

                return;
            }

            // Validate Tenant Status
            if (tenant.getStatus() != TenantStatus.ACTIVE) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Tenant is not active"
                );

                return;
            }

            // Validate Database Name
            String databaseName = tenant.getDatabaseName();

            if (databaseName == null ||
                    !databaseName.matches("^[a-zA-Z0-9_]{1,64}$")) {

                response.sendError(
                        HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                        "Tenant database is unavailable"
                );

                return;
            }

            // Initialize Tenant Context
            TenantContext.set(databaseName);

            log.info(
                    "Tenant resolved: {} - Thread: {}",
                    slug,
                    Thread.currentThread().getName()
            );

            // Continue Request
            filterChain.doFilter(request, response);

        } finally {

            TenantContext.clear();
        }
    }

    private String extractSlug(String host) {

        String suffix = "." +
                baseDomain.toLowerCase(Locale.ROOT);

        if (!host.endsWith(suffix)) {
            return null;
        }

        String slug = host.substring(
                0,
                host.length() - suffix.length()
        );

        if (!slug.matches(
                "^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$"
        )) {
            return null;
        }

        return slug;
    }
}