package com.hccake.ballcat.common.swagger;

import com.hccake.ballcat.common.swagger.property.SwaggerProviderProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:03
 */
@Import(SwaggerConfiguration.class)
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerProviderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProviderProperties swaggerProviderProperties() {
        return new SwaggerProviderProperties();
    }

    /**
     *  允许swagger文档跨域访问
     *  解决聚合文档导致的跨域问题
     * @return
     */
    @Bean
    @ConditionalOnBean(SwaggerProviderProperties.class)
    public FilterRegistrationBean corsFilter(SwaggerProviderProperties swaggerProviderProperties) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin(swaggerProviderProperties.getAggregatorOrigin());
        source.registerCorsConfiguration("/v2/api-docs**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
