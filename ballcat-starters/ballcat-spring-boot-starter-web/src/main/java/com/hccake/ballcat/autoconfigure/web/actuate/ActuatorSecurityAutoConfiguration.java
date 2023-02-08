package com.hccake.ballcat.autoconfigure.web.actuate;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 当开启 Actuator 时，注册 Actuator 安全过滤器
 *
 * @author Hccake 2019/10/17 20:26
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnClass(WebEndpointProperties.class)
@ConditionalOnProperty(prefix = ActuatorSecurityProperties.PREFIX, name = "auth", havingValue = "true")
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
			if (CharSequenceUtil.isBlank(basePath)) {
				basePath = "/actuator";
			}
			registrationBean.addUrlPatterns(basePath + "/*");
			registrationBean.setOrder(0);
		}

		return registrationBean;
	}

}
