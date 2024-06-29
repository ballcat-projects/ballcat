/*
 * Copyright 2023-2024 the original author or authors.
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

package org.ballcat.autoconfigure.web.servlet;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.web.pageable.PageableProperties;
import org.ballcat.web.pageable.DefaultPageParamArgumentResolver;
import org.ballcat.web.pageable.PageParamArgumentResolver;
import org.ballcat.web.pageable.PageableRequestOptions;
import org.ballcat.web.trace.TraceIdFilter;
import org.ballcat.web.trace.TraceIdGenerator;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Hccake 2019/10/19 17:10
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({ WebProperties.class, PageableProperties.class })
public class WebMvcAutoConfiguration {

	private final WebProperties webProperties;

	@Bean
	@ConditionalOnMissingBean
	public PageParamArgumentResolver pageParamArgumentResolver(PageableProperties pageableProperties) {
		PageableRequestOptions pageableRequestOptions = new PageableRequestOptions()
			.setMaxPageSize(pageableProperties.getMaxPageSize())
			.setPageParameterName(pageableProperties.getPageParameterName())
			.setSizeParameterName(pageableProperties.getSizeParameterName())
			.setSortParameterName(pageableProperties.getSortParameterName());
		return new DefaultPageParamArgumentResolver(pageableRequestOptions);
	}

	/**
	 * 允许聚合者对提供者的文档进行跨域访问 解决聚合文档导致的跨域问题
	 * @return FilterRegistrationBean
	 */
	@Bean
	@ConditionalOnProperty(prefix = WebProperties.PREFIX + ".cors-config", name = "enabled", havingValue = "true")
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		// 获取 CORS 配置
		WebProperties.CorsConfig corsConfig = this.webProperties.getCorsConfig();

		// 转换 CORS 配置
		CorsConfiguration corsConfiguration = getCorsConfiguration(corsConfig);

		// 注册 CORS 配置与资源的映射关系
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(corsConfig.getUrlPattern(), corsConfiguration);

		// 注册 CORS 过滤器，设置最高优先级 + 1 (在 traceId 之后)
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1000);

		return bean;
	}

	private static CorsConfiguration getCorsConfiguration(WebProperties.CorsConfig corsConfig) {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(corsConfig.getAllowedOrigins());
		corsConfiguration.setAllowedOriginPatterns(corsConfig.getAllowedOriginPatterns());
		corsConfiguration.setAllowedMethods(corsConfig.getAllowedMethods());
		corsConfiguration.setAllowedHeaders(corsConfig.getAllowedHeaders());
		corsConfiguration.setExposedHeaders(corsConfig.getExposedHeaders());
		corsConfiguration.setAllowCredentials(corsConfig.getAllowCredentials());
		corsConfiguration.setMaxAge(corsConfig.getMaxAge());
		return corsConfiguration;
	}

	@RequiredArgsConstructor
	@Configuration(proxyBeanMethods = false)
	static class CustomWebMvcConfigurer implements WebMvcConfigurer {

		private final PageParamArgumentResolver pageParamArgumentResolver;

		/**
		 * Page Sql注入过滤
		 * @param argumentResolvers 方法参数解析器集合
		 */
		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(this.pageParamArgumentResolver);
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class TraceIdConfiguration {

		@Bean
		@ConditionalOnMissingBean(TraceIdGenerator.class)
		public TraceIdGenerator traceIdGenerator() {
			return () -> ObjectId.get().toString();
		}

		@Bean
		public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean(WebProperties webProperties,
				TraceIdGenerator traceIdGenerator) {
			String traceIdHeaderName = webProperties.getTraceIdHeaderName();
			TraceIdFilter traceIdFilter = new TraceIdFilter(traceIdHeaderName, traceIdGenerator);
			FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>(traceIdFilter);
			registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
			return registrationBean;
		}

	}

}
