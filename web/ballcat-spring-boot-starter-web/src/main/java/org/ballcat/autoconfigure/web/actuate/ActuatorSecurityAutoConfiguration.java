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

package org.ballcat.autoconfigure.web.actuate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.web.actuate.ActuatorSecurityFilter;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

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
			String basePath = this.webEndpointProperties.getBasePath();
			if (!StringUtils.hasText(basePath)) {
				basePath = "/actuator";
			}
			registrationBean.addUrlPatterns(basePath + "/*");
			registrationBean.setOrder(0);
		}

		return registrationBean;
	}

}
