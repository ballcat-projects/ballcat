package com.hccake.ballcat.autoconfigure.web.actuate;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 当开启 Actuator 时，注册 Actuator 安全过滤器
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:26
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnClass(WebEndpointProperties.class)
@ConditionalOnProperty(prefix = ActuatorSecurityProperties.PREFIX, name = "auth", havingValue = "true")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ActuatorSecurityProperties.class)
public class ActuatorSecurityAutoConfiguration {

	private final WebEndpointProperties webEndpointProperties;

	@Bean
	public FilterRegistrationBean<ActuatorSecurityFilter> actuatorFilterRegistrationBean(
			ActuatorSecurityProperties actuatorSecurityProperties) {
		log.debug("Actuator 过滤器已开启====");
		FilterRegistrationBean<ActuatorSecurityFilter> registrationBean = new FilterRegistrationBean<>();
		if (actuatorSecurityProperties.isAuth()) {
			// 监控开启
			ActuatorSecurityFilter filter = new ActuatorSecurityFilter(actuatorSecurityProperties.getSecretId(),
					actuatorSecurityProperties.getSecretKey());
			registrationBean.setFilter(filter);
			String basePath = webEndpointProperties.getBasePath();
			if (StrUtil.isBlank(basePath)) {
				basePath = "/actuator";
			}
			registrationBean.addUrlPatterns(basePath + "/*");
			registrationBean.setOrder(0);
		}

		return registrationBean;
	}

}
