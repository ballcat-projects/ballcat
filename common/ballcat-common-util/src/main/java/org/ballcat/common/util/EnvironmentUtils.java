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

package org.ballcat.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.PropertySource;

/**
 * @author lingting 2022/10/15 11:33
 */
public final class EnvironmentUtils {

	private EnvironmentUtils() {
	}

	public static final String ENVIRONMENT_NAME_REPLACE = "replaceEnvironment";

	@Getter
	@Setter
	private static ConfigurableEnvironment ENVIRONMENT;

	public static boolean containsProperty(String key) {
		return ENVIRONMENT.containsProperty(key);
	}

	public static String getProperty(String key) {
		return ENVIRONMENT.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return ENVIRONMENT.getProperty(key, defaultValue);
	}

	public static <T> T getProperty(String key, Class<T> targetType) {
		return ENVIRONMENT.getProperty(key, targetType);
	}

	public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		return ENVIRONMENT.getProperty(key, targetType, defaultValue);
	}

	public static String getRequiredProperty(String key) throws IllegalStateException {
		return ENVIRONMENT.getRequiredProperty(key);
	}

	public static <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
		return ENVIRONMENT.getRequiredProperty(key, targetType);
	}

	public static String resolvePlaceholders(String text) {
		return ENVIRONMENT.resolvePlaceholders(text);
	}

	public static String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
		return ENVIRONMENT.resolveRequiredPlaceholders(text);
	}

	public static void setActiveProfiles(String... profiles) {
		ENVIRONMENT.setActiveProfiles(profiles);
	}

	public static void addActiveProfile(String profile) {
		ENVIRONMENT.addActiveProfile(profile);
	}

	public static void setDefaultProfiles(String... profiles) {
		ENVIRONMENT.setDefaultProfiles(profiles);
	}

	public static MutablePropertySources getPropertySources() {
		return ENVIRONMENT.getPropertySources();
	}

	public static Map<String, Object> getSystemProperties() {
		return ENVIRONMENT.getSystemProperties();
	}

	public static Map<String, Object> getSystemEnvironment() {
		return ENVIRONMENT.getSystemEnvironment();
	}

	public static void merge(ConfigurableEnvironment parent) {
		ENVIRONMENT.merge(parent);
	}

	public static ConfigurableConversionService getConversionService() {
		return ENVIRONMENT.getConversionService();
	}

	public static void setConversionService(ConfigurableConversionService conversionService) {
		ENVIRONMENT.setConversionService(conversionService);
	}

	public static void setPlaceholderPrefix(String placeholderPrefix) {
		ENVIRONMENT.setPlaceholderPrefix(placeholderPrefix);
	}

	public static void setPlaceholderSuffix(String placeholderSuffix) {
		ENVIRONMENT.setPlaceholderSuffix(placeholderSuffix);
	}

	public static void setValueSeparator(String valueSeparator) {
		ENVIRONMENT.setValueSeparator(valueSeparator);
	}

	public static void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
		ENVIRONMENT.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
	}

	public static void setRequiredProperties(String... requiredProperties) {
		ENVIRONMENT.setRequiredProperties(requiredProperties);
	}

	public static void validateRequiredProperties() throws MissingRequiredPropertiesException {
		ENVIRONMENT.validateRequiredProperties();
	}

	public static String[] getActiveProfiles() {
		return ENVIRONMENT.getActiveProfiles();
	}

	public static String[] getDefaultProfiles() {
		return ENVIRONMENT.getDefaultProfiles();
	}

	public static boolean acceptsProfiles(Profiles profiles) {
		return ENVIRONMENT.acceptsProfiles(profiles);
	}

	public static Map<String, Object> getReplaceMapPropertySource() {
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
	public static boolean isProfile(String profile) {
		return ArrayUtils.contains(getActiveProfiles(), profile);
	}

}
