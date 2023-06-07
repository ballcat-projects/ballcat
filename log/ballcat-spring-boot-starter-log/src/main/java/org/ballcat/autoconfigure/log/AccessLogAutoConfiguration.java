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
package org.ballcat.autoconfigure.log;

import org.ballcat.autoconfigure.log.properties.AccessLogProperties;
import org.ballcat.log.access.filter.AccessLogFilter;
import org.ballcat.log.access.handler.AccessLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Hccake 2019/10/15 18:20
 */
@Slf4j
@ConditionalOnWebApplication
@RequiredArgsConstructor
@EnableConfigurationProperties(AccessLogProperties.class)
@ConditionalOnProperty(prefix = AccessLogProperties.PREFIX, name = "enabled", matchIfMissing = true,
		havingValue = "true")
public class AccessLogAutoConfiguration {

	private final AccessLogHandler<?> accessLogService;

	private final AccessLogProperties accessLogProperties;

	@Bean
	@ConditionalOnClass(AccessLogHandler.class)
	public FilterRegistrationBean<AccessLogFilter> accessLogFilterRegistrationBean() {
		log.debug("access log 记录拦截器已开启====");
		FilterRegistrationBean<AccessLogFilter> registrationBean = new FilterRegistrationBean<>(
				new AccessLogFilter(accessLogService, accessLogProperties.getIgnoreUrlPatterns()));
		registrationBean.setOrder(accessLogProperties.getFilterOrder());
		return registrationBean;
	}

}
