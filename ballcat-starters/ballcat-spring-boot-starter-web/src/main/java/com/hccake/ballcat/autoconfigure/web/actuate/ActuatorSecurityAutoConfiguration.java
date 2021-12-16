package com.hccake.ballcat.autoconfigure.web.actuate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:26
 */
@Slf4j
@EnableConfigurationProperties(ActuatorSecurityProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
public class ActuatorSecurityAutoConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "ballcat.actuator", name = "auth", havingValue = "true")
	public FilterRegistrationBean<ActuatorSecurityFilter> actuatorFilterRegistrationBean(
			ActuatorSecurityProperties actuatorSecurityProperties) {
		log.debug("Actuator 过滤器已开启====");
		FilterRegistrationBean<ActuatorSecurityFilter> registrationBean = new FilterRegistrationBean<>();
		if (actuatorSecurityProperties.isAuth()) {
			// 监控开启
			ActuatorSecurityFilter filter = new ActuatorSecurityFilter(actuatorSecurityProperties.getSecretId(),
					actuatorSecurityProperties.getSecretKey());
			registrationBean.setFilter(filter);
			registrationBean.addUrlPatterns("/actuator/*");
			registrationBean.setOrder(0);
		}

		return registrationBean;
	}

}
