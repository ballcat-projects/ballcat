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

package org.ballcat.autoconfigure.web.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author hccake
 */
@Data
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {

	public static final String PREFIX = "ballcat.web";

	/**
	 * traceId 的 http 头名称
	 */
	private String traceIdHeaderName = "X-Trace-Id";

	/**
	 * 跨域配置
	 */
	private CorsConfig corsConfig;

	/**
	 * <p>
	 * 跨域配置.
	 * </p>
	 *
	 * @see CorsConfiguration
	 */
	@Data
	public static class CorsConfig {

		/**
		 * 开启 Cors 跨域配置
		 */
		private boolean enabled = false;

		/**
		 * 跨域对应的 url 匹配规则
		 */
		private String urlPattern = "/**";

		/**
		 * 允许跨域的源
		 */
		private List<String> allowedOrigins;

		/**
		 * 允许跨域来源的匹配规则
		 */
		private List<String> allowedOriginPatterns;

		/**
		 * 允许跨域的方法列表
		 */
		private List<String> allowedMethods = new ArrayList<>(Collections.singletonList(CorsConfiguration.ALL));

		/**
		 * 允许跨域的头信息
		 */
		private List<String> allowedHeaders = new ArrayList<>(Collections.singletonList(CorsConfiguration.ALL));

		/**
		 * 额外允许跨域请求方获取的 response header 信息
		 */
		private List<String> exposedHeaders = new ArrayList<>(Collections.singletonList("traceId"));

		/**
		 * 是否允许跨域发送 Cookie
		 */
		private Boolean allowCredentials = true;

		/**
		 * CORS 配置缓存时间，用于控制浏览器端是否发起 Option 预检请求。 若配置此参数，在第一次获取到 CORS
		 * 的配置信息后，在过期时间内，浏览器将直接发出请求，跳过 option 预检
		 */
		private Long maxAge;

	}

}
