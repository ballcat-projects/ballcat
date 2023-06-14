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
package org.ballcat.autoconfigure.web.servlet;

import lombok.RequiredArgsConstructor;
import org.ballcat.web.pageable.DefaultPageParamArgumentResolver;
import org.ballcat.web.pageable.PageParamArgumentResolver;
import org.ballcat.autoconfigure.web.pageable.PageableProperties;
import org.ballcat.web.trace.TraceIdFilter;
import org.ballcat.web.trace.TraceIdGenerator;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Hccake 2019/10/19 17:10
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({ WebProperties.class, PageableProperties.class })
public class WebMvcAutoConfiguration {

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

	@Bean
	@ConditionalOnMissingBean
	public PageParamArgumentResolver pageParamArgumentResolver(PageableProperties pageableProperties) {
		return new DefaultPageParamArgumentResolver(pageableProperties.getMaxPageSize(),
				pageableProperties.getPageParameterName(), pageableProperties.getSizeParameterName(),
				pageableProperties.getSortParameterName());
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
			argumentResolvers.add(pageParamArgumentResolver);
		}

	}

}
