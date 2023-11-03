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
package org.ballcat.common.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lingting 2022/10/15 11:33
 */
@UtilityClass
public class EnvironmentUtils {

	public final String ENVIRONMENT_NAME_REPLACE = "replaceEnvironment";

	@Getter
	@Setter
	private static ConfigurableEnvironment environment;

	public boolean containsProperty(String key) {
		return environment.containsProperty(key);
	}

	public String getProperty(String key) {
		return environment.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return environment.getProperty(key, defaultValue);
	}

	public <T> T getProperty(String key, Class<T> targetType) {
		return environment.getProperty(key, targetType);
	}

	public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		return environment.getProperty(key, targetType, defaultValue);
	}

	public String getRequiredProperty(String key) throws IllegalStateException {
		return environment.getRequiredProperty(key);
	}

	public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
		return environment.getRequiredProperty(key, targetType);
	}

	public String resolvePlaceholders(String text) {
		return environment.resolvePlaceholders(text);
	}

	public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
		return environment.resolveRequiredPlaceholders(text);
	}

	public void setActiveProfiles(String... profiles) {
		environment.setActiveProfiles(profiles);
	}

	public void addActiveProfile(String profile) {
		environment.addActiveProfile(profile);
	}

	public void setDefaultProfiles(String... profiles) {
		environment.setDefaultProfiles(profiles);
	}

	public MutablePropertySources getPropertySources() {
		return environment.getPropertySources();
	}

	public Map<String, Object> getSystemProperties() {
		return environment.getSystemProperties();
	}

	public Map<String, Object> getSystemEnvironment() {
		return environment.getSystemEnvironment();
	}

	public void merge(ConfigurableEnvironment parent) {
		environment.merge(parent);
	}

	public ConfigurableConversionService getConversionService() {
		return environment.getConversionService();
	}

	public void setConversionService(ConfigurableConversionService conversionService) {
		environment.setConversionService(conversionService);
	}

	public void setPlaceholderPrefix(String placeholderPrefix) {
		environment.setPlaceholderPrefix(placeholderPrefix);
	}

	public void setPlaceholderSuffix(String placeholderSuffix) {
		environment.setPlaceholderSuffix(placeholderSuffix);
	}

	public void setValueSeparator(String valueSeparator) {
		environment.setValueSeparator(valueSeparator);
	}

	public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
		environment.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
	}

	public void setRequiredProperties(String... requiredProperties) {
		environment.setRequiredProperties(requiredProperties);
	}

	public void validateRequiredProperties() throws MissingRequiredPropertiesException {
		environment.validateRequiredProperties();
	}

	public String[] getActiveProfiles() {
		return environment.getActiveProfiles();
	}

	public String[] getDefaultProfiles() {
		return environment.getDefaultProfiles();
	}

	public boolean acceptsProfiles(Profiles profiles) {
		return environment.acceptsProfiles(profiles);
	}

	public Map<String, Object> getReplaceMapPropertySource() {
		MutablePropertySources propertySources = getPropertySources();
		MapPropertySource target = null;

		if (propertySources.contains(ENVIRONMENT_NAME_REPLACE)) {
			PropertySource<?> source = propertySources.get(ENVIRONMENT_NAME_REPLACE);
			if (source instanceof MapPropertySource) {
				target = (MapPropertySource) source;
			}
		}
		else {
			target = new MapPropertySource(ENVIRONMENT_NAME_REPLACE, new HashMap<>(16));
			propertySources.addFirst(target);
		}

		return Optional.ofNullable(target).map(MapPropertySource::getSource).orElse(null);
	}

	/**
	 * 判断当前是否为指定环境
	 * @param profile 指定环境
	 */
	public boolean isProfile(String profile) {
		return ArrayUtils.contains(getActiveProfiles(), profile);
	}

}
