package com.hccake.ballcat.autoconfigure.tenant;

import cn.hutool.core.text.StrPool;
import com.hccake.ballcat.common.tenant.core.TenantType;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 非数据源模式时动态数据源不加载
 *
 * @author soundhu
 */
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "enable", havingValue = "true")
public class DynamicDatasourceEnvPostProcessor implements EnvironmentPostProcessor {

	public static final String DYNAMIC_DATASOURCE_ENABLE = "spring.datasource.dynamic.enabled";

	public static final String TENANT_TYPE = "ballcat.db.tenant.tenantType";

	private static final String REPLACE_SOURCE_NAME = "replaceEnvironment";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String tenantType = environment.getProperty(TENANT_TYPE);
		if (TenantType.DATASOURCE.name().equals(tenantType)) {
			return;
		}
		Map<String, Object> map = new HashMap<>(1);
		map.put(DYNAMIC_DATASOURCE_ENABLE, Boolean.FALSE.toString());
		replaceProperties(environment.getPropertySources(), map);
	}

	private void replaceProperties(MutablePropertySources propertySources, Map<String, Object> map) {
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
