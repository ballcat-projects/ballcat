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

package org.ballcat.xss.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {

	public static final String PREFIX = "ballcat.security.xss";

	/**
	 * 是否开启
	 */
	private boolean enabled = true;

	/**
	 * xss 过滤包含的路径（Ant风格）
	 **/
	private Set<String> includePaths = Collections.singleton("/**");

	/**
	 * xss 需要排除的路径（Ant风格），优先级高于包含路径
	 **/
	private Set<String> excludePaths = new HashSet<>();

	/**
	 * 需要处理的 HTTP 请求方法集合
	 */
	private final Set<String> includeHttpMethods = new HashSet<>(
			Arrays.asList(HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name()));

}
