package com.hccake.ballcat.common.util;

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

	public static final String ENVIRONMENT_NAME_REPLACE = "replaceEnvironment";

	@Getter
	@Setter
	private static ConfigurableEnvironment environment;

	public static boolean containsProperty(String key) {
		return environment.containsProperty(key);
	}

	public static String getProperty(String key) {
		return environment.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return environment.getProperty(key, defaultValue);
	}

	public static <T> T getProperty(String key, Class<T> targetType) {
		return environment.getProperty(key, targetType);
	}

	public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		return environment.getProperty(key, targetType, defaultValue);
	}

	public static String getRequiredProperty(String key) throws IllegalStateException {
		return environment.getRequiredProperty(key);
	}

	public static <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
		return environment.getRequiredProperty(key, targetType);
	}

	public static String resolvePlaceholders(String text) {
		return environment.resolvePlaceholders(text);
	}

	public static String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
		return environment.resolveRequiredPlaceholders(text);
	}

	public static void setActiveProfiles(String... profiles) {
		environment.setActiveProfiles(profiles);
	}

	public static void addActiveProfile(String profile) {
		environment.addActiveProfile(profile);
	}

	public static void setDefaultProfiles(String... profiles) {
		environment.setDefaultProfiles(profiles);
	}

	public static MutablePropertySources getPropertySources() {
		return environment.getPropertySources();
	}

	public static Map<String, Object> getSystemProperties() {
		return environment.getSystemProperties();
	}

	public static Map<String, Object> getSystemEnvironment() {
		return environment.getSystemEnvironment();
	}

	public static void merge(ConfigurableEnvironment parent) {
		environment.merge(parent);
	}

	public static ConfigurableConversionService getConversionService() {
		return environment.getConversionService();
	}

	public static void setConversionService(ConfigurableConversionService conversionService) {
		environment.setConversionService(conversionService);
	}

	public static void setPlaceholderPrefix(String placeholderPrefix) {
		environment.setPlaceholderPrefix(placeholderPrefix);
	}

	public static void setPlaceholderSuffix(String placeholderSuffix) {
		environment.setPlaceholderSuffix(placeholderSuffix);
	}

	public static void setValueSeparator(String valueSeparator) {
		environment.setValueSeparator(valueSeparator);
	}

	public static void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
		environment.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
	}

	public static void setRequiredProperties(String... requiredProperties) {
		environment.setRequiredProperties(requiredProperties);
	}

	public static void validateRequiredProperties() throws MissingRequiredPropertiesException {
		environment.validateRequiredProperties();
	}

	public static String[] getActiveProfiles() {
		return environment.getActiveProfiles();
	}

	public static String[] getDefaultProfiles() {
		return environment.getDefaultProfiles();
	}

	public static boolean acceptsProfiles(Profiles profiles) {
		return environment.acceptsProfiles(profiles);
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
