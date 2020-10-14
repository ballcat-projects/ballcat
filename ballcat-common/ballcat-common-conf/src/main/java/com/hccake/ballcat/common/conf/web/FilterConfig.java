package com.hccake.ballcat.common.conf.web;

import com.hccake.ballcat.common.conf.config.MonitorProperties;
import com.hccake.ballcat.common.core.filter.ActuatorAuthFilter;
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
	@ConditionalOnProperty(prefix = "monitor.actuator.auth", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public FilterRegistrationBean<ActuatorAuthFilter> actuatorFilterRegistrationBean(MonitorProperties properties) {
		log.debug("Actuator 过滤器已开启====");
		FilterRegistrationBean<ActuatorAuthFilter> registrationBean = new FilterRegistrationBean<>();
		MonitorProperties.Actuator.Auth auth = properties.getActuator().getAuth();
		if (auth.getEnabled()) {
			// 监控开启
			ActuatorAuthFilter filter = new ActuatorAuthFilter(auth.getSecretId(), auth.getSecretKey());
			registrationBean.setFilter(filter);
			registrationBean.addUrlPatterns("/actuator/*");
			registrationBean.setOrder(0);
		}

		return registrationBean;
	}

}
