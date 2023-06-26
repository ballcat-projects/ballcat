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

import lombok.Data;
import org.ballcat.web.accesslog.AbstractAccessLogFilter;
import org.ballcat.web.accesslog.AccessLogRule;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 访问日志配置
 *
 * @author Hccake 2020/6/11 14:56
 */
@Data
@ConfigurationProperties(prefix = AccessLogProperties.PREFIX)
public class AccessLogProperties {

	public static final String PREFIX = "ballcat.web.accesslog";

	/**
	 * 开启 access log 的记录
	 */
	private boolean enabled = false;

	/**
	 * access log filter 的优先级
	 *
	 * @see org.springframework.core.Ordered
	 */
	private Integer filterOrder = -1000;

	/**
	 * 最大的 payload 长度
	 */
	private Integer maxPayloadLength = AbstractAccessLogFilter.DEFAULT_MAX_PAYLOAD_LENGTH;

	/**
	 * 访问日志的设置集合
	 */
	private List<AccessLogRule> settings = Collections.singletonList(new AccessLogRule().setUrlPattern("/**"));

	//
	// /**
	// * 忽略的Url匹配规则，Ant风格
	// */
	// private List<String> ignoreUrlPatterns = Arrays.asList("/actuator/**",
	// "/webjars/**", "/favicon.ico",
	// "/swagger-ui/**", "/bycdao-ui/**", "/captcha/get");

}