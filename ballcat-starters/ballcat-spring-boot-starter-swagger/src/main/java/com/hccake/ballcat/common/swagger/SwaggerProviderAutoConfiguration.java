/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hccake.ballcat.common.swagger;

import com.hccake.ballcat.common.swagger.property.SwaggerProperties;
import com.hccake.ballcat.common.swagger.property.SwaggerProviderProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/11/1 20:03
 */
@Import(SwaggerConfiguration.class)
@ConditionalOnProperty(prefix = SwaggerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
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
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}
