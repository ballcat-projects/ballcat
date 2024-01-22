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

package org.ballcat.autoconfigure.openapi;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * 将 ballcat openApi 的开关配置同步至 springdoc
 *
 * @author hccake
 */
public class SwaggerEnabledStatusReplaceEnvironmentPostProcessor implements EnvironmentPostProcessor {

	/**
	 * 资源名称
	 */
	private static final String REPLACE_SOURCE_NAME = "replaceEnvironment";

	private static final String BALLCAT_OPENAPI_ENABLED_KEY = OpenApiProperties.PREFIX + ".enabled";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 如果已经独立配置了 springdoc 的开关信息，则不处理
		String springFoxSwaggerEnabledValue = environment.getProperty(Constants.SPRINGDOC_ENABLED);
		if (StringUtils.hasText(springFoxSwaggerEnabledValue)) {
			return;
		}

		// 获取 ballcat 的 openApi 开关状态
		boolean ballcatEnabledOpenApi = true;
		String ballcatOpenApiEnabledValue = environment.getProperty(BALLCAT_OPENAPI_ENABLED_KEY);
		if (StringUtils.hasText(ballcatOpenApiEnabledValue)) {
			ballcatEnabledOpenApi = "true".equalsIgnoreCase(ballcatOpenApiEnabledValue);
		}

		// 将 ballcat openapi 的开关状态同步至 springdoc
		Map<String, Object> map = new HashMap<>(1);
		map.put(Constants.SPRINGDOC_ENABLED, ballcatEnabledOpenApi);
		replace(environment.getPropertySources(), map);
	}

	private void replace(MutablePropertySources propertySources, Map<String, Object> map) {
		MapPropertySource target = null;
		if (propertySources.contains(REPLACE_SOURCE_NAME)) {
			PropertySource<?> source = propertySources.get(REPLACE_SOURCE_NAME);
			if (source instanceof MapPropertySource) {
				target = (MapPropertySource) source;
				target.getSource().putAll(map);
			}
		}
		if (target == null) {
			target = new MapPropertySource(REPLACE_SOURCE_NAME, map);
		}
		if (!propertySources.contains(REPLACE_SOURCE_NAME)) {
			propertySources.addFirst(target);
		}
	}

}
