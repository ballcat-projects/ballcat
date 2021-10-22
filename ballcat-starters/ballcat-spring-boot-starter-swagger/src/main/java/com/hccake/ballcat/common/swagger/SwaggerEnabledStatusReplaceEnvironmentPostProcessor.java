package com.hccake.ballcat.common.swagger;

import com.hccake.ballcat.common.swagger.property.SwaggerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 将 ballcat swagger 的开关配置同步至 springfox
 *
 * @author hccake
 */
public class SwaggerEnabledStatusReplaceEnvironmentPostProcessor implements EnvironmentPostProcessor {

	/**
	 * 资源名称
	 */
	private static final String REPLACE_SOURCE_NAME = "replaceEnvironment";

	private static final String SPRINGFOX_SWAGGER_ENABLED_KEY = "springfox.documentation.enabled";

	private static final String BALLCAT_SWAGGER_ENABLED_KEY = SwaggerProperties.PREFIX + "enabled";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 如果已经独立配置了 springfox 的开关信息，则不处理
		String springFoxSwaggerEnabledValue = environment.getProperty(SPRINGFOX_SWAGGER_ENABLED_KEY);
		if (StringUtils.hasText(springFoxSwaggerEnabledValue)) {
			return;
		}

		// 获取 ballcat 的 swagger 开关状态
		boolean ballcatEnabledSwagger = true;
		String ballcatSwaggerEnabledValue = environment.getProperty(BALLCAT_SWAGGER_ENABLED_KEY);
		if (StringUtils.hasText(ballcatSwaggerEnabledValue)) {
			ballcatEnabledSwagger = "true".equalsIgnoreCase(ballcatSwaggerEnabledValue);
		}

		// 将 ballcat swagger 的开关状态同步至 springfox
		Map<String, Object> map = new HashMap<>(1);
		map.put(SPRINGFOX_SWAGGER_ENABLED_KEY, ballcatEnabledSwagger);
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
