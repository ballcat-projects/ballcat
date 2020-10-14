package com.hccake.ballcat.common.conf.web;

import com.hccake.ballcat.common.conf.config.MonitorProperties;
import com.hccake.ballcat.common.core.filter.ActuatorFilter;
import com.hccake.ballcat.common.core.filter.XSSFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:26
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
public class FilterConfig {

	@Bean
	@ConditionalOnProperty(prefix = "ballcat.security.xss", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public FilterRegistrationBean<XSSFilter> xssFilterRegistrationBean() {
		log.debug("XSS 过滤已开启====");
		FilterRegistrationBean<XSSFilter> registrationBean = new FilterRegistrationBean<>(new XSSFilter());
		registrationBean.setOrder(-1);
		return registrationBean;
	}

	@Bean
	@ConditionalOnProperty(prefix = "monitor", name = "enabled", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean<ActuatorFilter> actuatorFilterRegistrationBean(MonitorProperties properties) {
		log.debug("Actuator 过滤器已开启====");
		FilterRegistrationBean<ActuatorFilter> registrationBean = new FilterRegistrationBean<>();

		if (properties.getEnabled()) {
			// 监控开启
			ActuatorFilter actuatorFilter = new ActuatorFilter(properties.getSecretId(), properties.getSecretKey());
			registrationBean.setFilter(actuatorFilter);
			registrationBean.addUrlPatterns("/actuator/*");
			registrationBean.setOrder(0);
		}

		return registrationBean;
	}

}
