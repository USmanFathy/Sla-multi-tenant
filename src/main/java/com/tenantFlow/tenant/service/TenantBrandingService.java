package com.tenantFlow.tenant.service;

import com.tenantFlow.storage.StorageService;
import com.tenantFlow.tenant.model.dto.request.CreateTenantBrandingRequest;
import com.tenantFlow.tenant.model.entity.Tenant;
import com.tenantFlow.tenant.model.entity.TenantBranding;
import com.tenantFlow.tenant.model.enums.BrandingStatus;
import com.tenantFlow.tenant.model.mapper.TenantBrandingMapper;
import com.tenantFlow.tenant.repository.TenantBrandingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class TenantBrandingService {

    private final TenantBrandingRepository brandingRepository;

    private final TenantBrandingMapper brandingMapper;

    private final StorageService storageService;
    public void createInitialBranding(
            Tenant tenant,
            CreateTenantBrandingRequest request
    ) {
        registerTransactionCallback();
        TenantBranding branding = brandingMapper.toEntity(request);

        branding.setTenant(tenant);
        branding.setVersion(1);
        branding.setStatus(BrandingStatus.DRAFT);

        String directory = "tenants/"
                + tenant.getId()
                + "/branding";

        if (request.getLogo() != null
                && !request.getLogo().isEmpty()) {

            branding.setLogoKey(
                    storageService.upload(
                            request.getLogo(), directory
                    )
            );
        }

        if (request.getFavicon() != null
                && !request.getFavicon().isEmpty()) {

            branding.setFaviconKey(
                    storageService.upload(
                            request.getFavicon(), directory
                    )
            );
        }

        brandingRepository.save(branding);
    }
    private void registerTransactionCallback() {

        TransactionSynchronizationManager
                .registerSynchronization(
                        new TransactionSynchronization() {

                            @Override
                            public void afterCompletion(int status) {

                                if (status == STATUS_COMMITTED) {
                                    System.out.println("Transaction committed");
                                }

                                if (status == STATUS_ROLLED_BACK) {
                                    System.out.println("Transaction rolled back");
                                }
                            }
                        }
                );
    }
}