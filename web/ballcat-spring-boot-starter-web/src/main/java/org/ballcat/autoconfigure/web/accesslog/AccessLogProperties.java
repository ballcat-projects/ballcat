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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.web.accesslog.AbstractAccessLogFilter;
import org.ballcat.web.accesslog.AccessLogRecordOptions;
import org.ballcat.web.accesslog.AccessLogRule;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * 访问日志配置
 *
 * @author Hccake 2020/6/11 14:56
 */
@Slf4j
@Data
@Setter
@ConfigurationProperties(prefix = AccessLogProperties.PREFIX)
public class AccessLogProperties {

	public static final String PREFIX = "ballcat.web.accesslog";

	/**
	 * 开启 access log 的记录
	 */
	private boolean enabled = false;

	/**
	 * 默认的访问日志过滤器写日志时的级别
	 */
	private Level defaultFilterLogLevel = Level.DEBUG;

	/**
	 * access log filter 的优先级
	 *
	 * @see org.springframework.core.Ordered
	 */
	private Integer filterOrder = -1000;

	/**
	 * 自动注册 access log filter
	 */
	private Boolean filterAutoRegister = true;

	/**
	 * 访问日志记录的默认选项，当请求路径无法在 rules 中匹配时，使用该选项
	 */
	private RecordOptions defaultRecordOptions = new RecordOptions();

	/**
	 * 访问日志记录的规则列表
	 * <p>
	 * 以当前 request uri 匹配中的第一个规则为准，所以通用性的规则应放在最后一项, 例如 /a/b 应在 /a/** 之前
	 */
	private List<Rule> rules = Collections.emptyList();

	public AccessLogRecordOptions getDefaultAccessLogRecordOptions() {
		return convertToAccessLogRecordOptions(this.defaultRecordOptions);
	}

	public List<AccessLogRule> getAccessLogRules() {
		return this.rules.stream()
			.filter(this::isValidRule)
			.map(AccessLogProperties::convertToAccessLogRule)
			.collect(Collectors.toList());
	}

	private boolean isValidRule(Rule rule) {
		if (!StringUtils.hasText(rule.getUrlPattern())) {
			log.warn("Access log rule urlPattern is empty, ignore it.");
			return false;
		}
		if (rule.getRecordOptions() == null) {
			log.warn("Access log rule recordOptions is null, ignore it. urlPattern: {}", rule.getUrlPattern());
			return false;
		}
		return true;
	}

	private static AccessLogRecordOptions convertToAccessLogRecordOptions(RecordOptions recordOptions) {
		return AccessLogRecordOptions.builder()
			.ignored(recordOptions.isIgnored())
			.includeQueryString(recordOptions.isIncludeQueryString())
			.includeRequestBody(recordOptions.isIncludeRequestBody())
			.includeResponseBody(recordOptions.isIncludeResponseBody())
			.maxRequestBodyLength(recordOptions.getMaxRequestBodyLength())
			.maxResponseBodyLength(recordOptions.getMaxResponseBodyLength())
			.build();
	}

	private static AccessLogRule convertToAccessLogRule(Rule rule) {
		AccessLogRecordOptions logRecordOptions = convertToAccessLogRecordOptions(rule.getRecordOptions());
		return new AccessLogRule(rule.getUrlPattern(), logRecordOptions);
	}

	@Data
	static class RecordOptions {

		/**
		 * 忽略记录
		 */
		private boolean ignored = false;

		/**
		 * 记录查询参数
		 */
		private boolean includeQueryString = true;

		/**
		 * 记录请求体
		 */
		private boolean includeRequestBody = false;

		/**
		 * 记录响应体
		 */
		private boolean includeResponseBody = false;

		/**
		 * 记录的最大的请求 body 长度
		 */
		private Integer maxRequestBodyLength = AbstractAccessLogFilter.DEFAULT_MAX_BODY_LENGTH;

		/**
		 * 记录的最大的响应 body 长度
		 */
		private Integer maxResponseBodyLength = AbstractAccessLogFilter.DEFAULT_MAX_BODY_LENGTH;

	}

	@Data
	static class Rule {

		/**
		 * 当前设置匹配的 url 规则（Ant风格）
		 */
		private String urlPattern;

		/**
		 * 访问日志记录选项
		 */
		private RecordOptions recordOptions;

	}

}
