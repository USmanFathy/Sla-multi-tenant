package com.tenantFlow.tenant.model.entity;

import com.tenantFlow.tenant.model.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 6)
    private String code;
    @Column(name = "database_name",
            unique = true, length = 64)
    private String databaseName;

    @Column(nullable = false, unique = true, length = 63)
    private String slug;

    @Column(nullable = false, length = 254)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;
    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY)
    private List<TenantBranding> brandings = new ArrayList<>();
}