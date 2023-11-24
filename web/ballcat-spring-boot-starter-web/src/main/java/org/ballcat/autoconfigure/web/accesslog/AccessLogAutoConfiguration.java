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
package org.ballcat.autoconfigure.web.accesslog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.web.accesslog.AbstractAccessLogFilter;
import org.ballcat.web.accesslog.AccessLogRule;
import org.ballcat.web.accesslog.DefaultAccessLogFilter;
import org.ballcat.web.accesslog.annotation.AccessLoggingRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Hccake 2019/10/15 18:20
 */
@Slf4j
@ConditionalOnWebApplication
@RequiredArgsConstructor
@EnableConfigurationProperties(AccessLogProperties.class)
@ConditionalOnProperty(prefix = AccessLogProperties.PREFIX, name = "enabled", havingValue = "true")
public class AccessLogAutoConfiguration {

	private final AccessLogProperties accessLogProperties;

	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Bean
	@ConditionalOnMissingBean(AbstractAccessLogFilter.class)
	public AbstractAccessLogFilter defaultAccessLogFilter() {

		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

		List<AccessLogRule> accessLoggingRules = new ArrayList<>();

		// 遍历 handlerMethods，获取所有的方法
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo mappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();

			// 获取方法上的 AccessLoggingRule 注解
			AccessLoggingRule accessLoggingRule = handlerMethod.getMethodAnnotation(AccessLoggingRule.class);

			// 如果方法上没有注解，则获取类上的注解
			if (accessLoggingRule == null) {
				accessLoggingRule = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AccessLoggingRule.class);
			}

			if (accessLoggingRule != null && mappingInfo.getPatternsCondition() != null ) {
				for (String pattern : mappingInfo.getPatternsCondition().getPatterns()) {
					AccessLogRule accessLogRule = new AccessLogRule()
							.setUrlPattern(pattern)
							.setIgnore(accessLoggingRule.ignore())
							.setIncludeQueryString(accessLoggingRule.includeQueryString())
							.setIncludeRequestBody(accessLoggingRule.includeRequestBody())
							.setIncludeResponseBody(accessLoggingRule.includeResponseBody());

					accessLoggingRules.add(accessLogRule);
				}
			}
		}

		List<AccessLogRule> settings = accessLogProperties.getSettings();

		// 合并 accessLoggingRules 和 settings,当 urlPattern 相同时优先使用注解的值
		Map<String, AccessLogRule> mergerAccessLogRuleMap = Stream.concat(accessLoggingRules.stream(),
						settings.stream())
				.collect(Collectors.toMap(AccessLogRule::getUrlPattern, Function.identity(), (v1, v2) -> v1));

		List<AccessLogRule> mergedList = new ArrayList<>(mergerAccessLogRuleMap.values());

		AbstractAccessLogFilter accessLogFilter = new DefaultAccessLogFilter(mergedList);
		accessLogFilter.setMaxBodyLength(accessLogProperties.getMaxBodyLength());
		accessLogFilter.setOrder(accessLogProperties.getFilterOrder());
		return accessLogFilter;
	}

}
