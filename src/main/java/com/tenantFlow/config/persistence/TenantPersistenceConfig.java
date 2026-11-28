package com.tenantFlow.config.persistence;

import com.tenantFlow.tenant.datasource.TenantDataSourceManager;
import com.tenantFlow.tenant.datasource.TenantRoutingDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tenantFlow.workspace.repository",
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager"
)
public class TenantPersistenceConfig {

    @Bean(name = "tenantDataSource")
    public DataSource tenantDataSource(
            TenantDataSourceManager dataSourceManager
    ) {
        return new TenantRoutingDataSource(dataSourceManager);
    }

    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    tenantEntityManagerFactory(
            EntityManagerFactoryBuilder builder,

            @Qualifier("tenantDataSource")
            DataSource dataSource
    ) {

        Map<String, Object> properties = Map.of(
                "hibernate.boot.allow_jdbc_metadata_access", false,
                "hibernate.dialect", "org.hibernate.dialect.MySQLDialect",
                "hibernate.hbm2ddl.auto", "none",
                "jakarta.persistence.database-major-version", 8,
                "jakarta.persistence.database-minor-version", 0
        );

        return builder
                .dataSource(dataSource)
                .packages("com.tenantFlow.workspace.model.entity")
                .persistenceUnit("tenant")
                .properties(properties)
                .build();
    }
    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory")
            EntityManagerFactory entityManagerFactory
    ) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}