package com.hccake.ballcat.autoconfigure.web.servlet;

import com.hccake.ballcat.autoconfigure.web.pageable.DefaultPageParamArgumentResolver;
import com.hccake.ballcat.autoconfigure.web.pageable.PageParamArgumentResolver;
import com.hccake.ballcat.autoconfigure.web.pageable.PageableProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/19 17:10
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(PageableProperties.class)
public class WebMvcAutoConfiguration {

	private final PageableProperties pageableProperties;

	@Bean
	@ConditionalOnMissingBean
	public PageParamArgumentResolver pageParamArgumentResolver() {
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
