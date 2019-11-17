package com.hccake.ballcat.common.swagger;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:03
 */
@Configuration
@EnableAutoConfiguration
@Import(SwaggerConfiguration.class)
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerAggregatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProviderProperties swaggerProviderProperties() {
        return new SwaggerProviderProperties();
    }

    /**
     * 聚合文档
     *
     * @param defaultResourcesProvider
     * @return
     */
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(
            InMemorySwaggerResourcesProvider defaultResourcesProvider,
            SwaggerProviderProperties swaggerProviderProperties) {

        return () -> {
            // 聚合者自己的 Resources
            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
            // 提供者的 Resources
            List<SwaggerResource> providerResources = swaggerProviderProperties.getResources();
            if (!CollectionUtils.isEmpty(providerResources)){
                resources.addAll(providerResources);
            }
            return resources;
        };
    }





}
