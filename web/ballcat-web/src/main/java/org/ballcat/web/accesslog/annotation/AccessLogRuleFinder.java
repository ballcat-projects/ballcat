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

package org.ballcat.web.accesslog.annotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ballcat.web.accesslog.AccessLogRecordOptions;
import org.ballcat.web.accesslog.AccessLogRule;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Hccake
 * @since 2.0.0
 */
public final class AccessLogRuleFinder {

	private AccessLogRuleFinder() {
	}

	/**
	 * 转换 AccessLoggingRule 注解为对应的 AccessLogRule
	 */
	public static List<AccessLogRule> findRulesFormAnnotation(
			RequestMappingHandlerMapping requestMappingHandlerMapping) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

		List<AccessLogRule> annotationRules = new ArrayList<>();

		// 遍历 handlerMethods，获取所有的方法
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo mappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();

			// 获取方法上的 AccessLoggingRule 注解
			AccessLoggingRule accessLoggingRule = handlerMethod.getMethodAnnotation(AccessLoggingRule.class);

			// 如果方法上没有注解，则获取类上的注解
			if (accessLoggingRule == null) {
				accessLoggingRule = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),
						AccessLoggingRule.class);
			}

			if (accessLoggingRule != null) {
				for (String pattern : mappingInfo.getPatternValues()) {
					AccessLogRecordOptions logRecordOptions = convertToRecordOptions(accessLoggingRule);
					AccessLogRule accessLogRule = new AccessLogRule(pattern, logRecordOptions);
					annotationRules.add(accessLogRule);
				}
			}
		}

		return annotationRules;
	}

	public static AccessLogRecordOptions convertToRecordOptions(AccessLoggingRule accessLoggingRule) {
		return AccessLogRecordOptions.builder()
			.ignored(accessLoggingRule.ignored())
			.includeQueryString(accessLoggingRule.includeQueryString())
			.includeRequestBody(accessLoggingRule.includeRequestBody())
			.includeResponseBody(accessLoggingRule.includeResponseBody())
			.maxRequestBodyLength(accessLoggingRule.maxRequestBodyLength())
			.maxResponseBodyLength(accessLoggingRule.maxResponseBodyLength())
			.build();
	}

	/**
	 * 合并两个 AccessLogRule 集合，并根据 urlPattern 去重，优先保留 rules1 中的规则
	 */
	public static List<AccessLogRule> mergeRules(List<AccessLogRule> rules1, List<AccessLogRule> rules2) {
		Set<String> urlPatterns = new HashSet<>();
		return Stream.concat(rules1.stream(), rules2.stream())
			.filter(accessLogRule -> urlPatterns.add(accessLogRule.getUrlPattern()))
			.collect(Collectors.toList());
	}

}
