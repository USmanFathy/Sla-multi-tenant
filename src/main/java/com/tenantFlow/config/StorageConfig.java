package com.tenantFlow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
@ConditionalOnProperty(
        name = "storage.driver",
        havingValue = "local",
        matchIfMissing = true
)
public class StorageConfig implements WebMvcConfigurer {

    @Value("${storage.local.root}")
    private String root;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        String location = Path.of(root)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        location.endsWith("/")
                                ? location
                                : location + "/"
                );
    }
}