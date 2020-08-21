package com.hccake.ballcat.common.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lingting 2020/6/12 16:36
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

	public static String[] getActiveProfiles() {
		return context.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取环境
	 *
	 * @author lingting 2020-06-12 16:38:56
	 */
	public static Environment getEnvironment() {
		return context.getEnvironment();
	}

	public static ApplicationContext getContext() {
		return context;
	}

}
