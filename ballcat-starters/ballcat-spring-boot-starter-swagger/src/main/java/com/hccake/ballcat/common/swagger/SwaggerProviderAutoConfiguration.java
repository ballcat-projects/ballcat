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
@ConditionalOnProperty(name = "ballcat.swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerProviderAutoConfiguration {

	private static final String ALL = "*";

	@Bean
	@ConditionalOnMissingBean
	public SwaggerProviderProperties swaggerProviderProperties() {
		return new SwaggerProviderProperties();
	}

	/**
	 * 允许swagger文档跨域访问 解决聚合文档导致的跨域问题
	 * @return FilterRegistrationBean
	 */
	@Bean
	@ConditionalOnBean(SwaggerProviderProperties.class)
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(
			SwaggerProviderProperties swaggerProviderProperties) {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		String aggregatorOrigin = swaggerProviderProperties.getAggregatorOrigin();
		config.setAllowCredentials(true);
		// 在 springmvc 5.3 版本之后，跨域来源使用 * 号进行匹配的方式进行调整
		if (ALL.equals(aggregatorOrigin)) {
			config.addAllowedOriginPattern(ALL);
		}
		else {
			config.addAllowedOrigin(aggregatorOrigin);
		}
		config.addAllowedHeader(ALL);
		config.addAllowedMethod(ALL);
		source.registerCorsConfiguration("/v2/api-docs**", config);
		source.registerCorsConfiguration("/v3/api-docs**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

}
