package com.hccake.ballcat.common.conf.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/19 17:10
 */
@EnableAsync
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * Page Sql注入过滤
	 * @param argumentResolvers 方法参数解析器集合
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PageParamArgumentResolver());
		argumentResolvers.add(new IPageArgumentResolver());
	}

}
