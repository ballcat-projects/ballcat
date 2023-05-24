package com.hccake.ballcat.autoconfigure.web.servlet;

import cn.hutool.core.util.IdUtil;
import com.hccake.ballcat.autoconfigure.web.pageable.DefaultPageParamArgumentResolver;
import com.hccake.ballcat.autoconfigure.web.pageable.PageParamArgumentResolver;
import com.hccake.ballcat.autoconfigure.web.pageable.PageableProperties;
import com.hccake.ballcat.autoconfigure.web.trace.TraceIdGenerator;
import com.hccake.ballcat.autoconfigure.web.trace.TraceIdFilter;
import lombok.RequiredArgsConstructor;
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
			return IdUtil::objectId;
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
		return new DefaultPageParamArgumentResolver(pageableProperties);
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
