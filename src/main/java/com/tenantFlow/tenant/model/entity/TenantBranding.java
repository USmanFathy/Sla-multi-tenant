package com.tenantFlow.tenant.model.entity;

import com.tenantFlow.tenant.model.enums.BrandingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tenant_brandings",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"tenant_id", "version"}
                )
        }
)
public class TenantBranding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private int version;

    private String logoKey;

    private String faviconKey;

    @Column(length = 7)
    private String primaryColor;

    @Column(length = 7)
    private String secondaryColor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BrandingStatus status;
    @Column(nullable = false, updatable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}