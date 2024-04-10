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

package org.ballcat.autoconfigure.web.accesslog;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.web.accesslog.AccessLogFilter;
import org.ballcat.web.accesslog.AccessLogRecordOptions;
import org.ballcat.web.accesslog.AccessLogRule;
import org.ballcat.web.accesslog.annotation.AccessLogRuleFinder;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Hccake 2019/10/15 18:20
 */
@Slf4j
@ImportAutoConfiguration(AccessLogAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AccessLogTestConfiguration {

	private final AccessLogProperties accessLogProperties;

	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	/**
	 * 注册 AccessLogFilter 过滤器
	 * @return FilterRegistrationBean<AccessLogFilter>
	 */
	@Bean
	public AccessLogFilter accessLogFilter() {
		// 合并 annotationRules 和 propertiesRules, 注解高于配置
		List<AccessLogRule> annotationRules = AccessLogRuleFinder
			.findRulesFormAnnotation(this.requestMappingHandlerMapping);
		List<AccessLogRule> propertiesRules = this.accessLogProperties.getAccessLogRules();
		List<AccessLogRule> accessLogRules = AccessLogRuleFinder.mergeRules(annotationRules, propertiesRules);

		AccessLogRecordOptions defaultRecordOptions = this.accessLogProperties.getDefaultAccessLogRecordOptions();

		TestAccessLogFilter accessLogFilter = new TestAccessLogFilter(defaultRecordOptions, accessLogRules);
		accessLogFilter.setOrder(this.accessLogProperties.getFilterOrder());
		return accessLogFilter;
	}

}
