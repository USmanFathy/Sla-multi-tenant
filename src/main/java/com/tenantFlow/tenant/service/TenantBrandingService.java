package com.tenantFlow.tenant.service;

import com.tenantFlow.storage.StorageService;
import com.tenantFlow.tenant.model.dto.request.CreateTenantBrandingRequest;
import com.tenantFlow.tenant.model.dto.response.TenantBrandingResponse;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.entity.TenantBranding;
import com.tenantFlow.tenant.model.enums.BrandingStatus;
import com.tenantFlow.tenant.model.mapper.TenantBrandingMapper;
import com.tenantFlow.tenant.repository.TenantBrandingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantBrandingService {

    private final TenantBrandingRepository brandingRepository;

    private final TenantBrandingMapper brandingMapper;

    private final StorageService storageService;
    @Transactional(propagation = Propagation.MANDATORY)
    public void createInitialBranding(
            Tenant tenant,
            CreateTenantBrandingRequest request
    ) {
        List<String> uploadedKeys = new ArrayList<>();
        registerFileCleanup(uploadedKeys);
        TenantBranding branding = brandingMapper.toEntity(request);

        branding.setTenant(tenant);
        branding.setVersion(1);
        branding.setStatus(BrandingStatus.DRAFT);

        String directory = "tenants/"
                + tenant.getId()
                + "/branding";

        if (request.getLogo() != null
                && !request.getLogo().isEmpty()) {
            String logoKey = storageService.upload(
                    request.getLogo(), directory
            );

            uploadedKeys.add(logoKey);

            branding.setLogoKey(
                    logoKey
            );
        }

        if (request.getFavicon() != null
                && !request.getFavicon().isEmpty()) {

            String faviconKey = storageService.upload(
                    request.getFavicon(), directory
            );

            uploadedKeys.add(faviconKey);

            branding.setFaviconKey(faviconKey);
        }

        brandingRepository.save(branding);
    }
    private void registerFileCleanup(List<String> uploadedKeys) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCompletion(int status) {

                        if (status == STATUS_ROLLED_BACK) {

                            for (String key : uploadedKeys) {
                                try {
                                    storageService.delete(key);
                                } catch (RuntimeException e) {
                                    log.error("Failed to delete file: {}", key, e);
                                }
                            }

                        }
                    }
                }
        );
    }
    public List<TenantBrandingResponse> getBrandings(UUID tenantId) {

        List<TenantBranding> brandings =
                brandingRepository
                        .findAllByTenant_IdOrderByVersionDesc(tenantId);

        return brandings.stream()
                .map(this::buildBrandingResponse)
                .toList();
    }
    private TenantBrandingResponse buildBrandingResponse(
            TenantBranding branding
    ) {

        TenantBrandingResponse response =
                brandingMapper.toResponse(branding);

        if (branding.getLogoKey() != null) {

            response.setLogoUrl(
                    storageService.getUrl(branding.getLogoKey())
            );
        }

        if (branding.getFaviconKey() != null) {

            response.setFaviconUrl(
                    storageService.getUrl(branding.getFaviconKey())
            );
        }

        return response;
    }
}