package com.tenantFlow.config.persistence;
import javax.sql.DataSource;

import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tenantFlow.tenant.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class CentralPersistenceConfig {

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    centralEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("centralDataSource")
            DataSource dataSource    ) {

        return builder
                .dataSource(dataSource)
                .packages("com.tenantFlow.tenant.model.entity")
                .persistenceUnit("central")
                .build();
    }
    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager centralTransactionManager(
            @Qualifier("entityManagerFactory")
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    @Primary
    @Bean(name = "centralDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource centralDataSource(
            DataSourceProperties properties
    ) {

        return properties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}