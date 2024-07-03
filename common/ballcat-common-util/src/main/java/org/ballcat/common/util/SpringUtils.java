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

import java.util.Map;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2020/6/12 16:36
 */
@Component(SpringUtils.BEAN_NAME)
public class SpringUtils implements ApplicationContextAware {

	public static final String BEAN_NAME = "ballcatSpringUtils";

	/**
	 * Spring应用上下文环境
	 */
	@Setter
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		setContext(context);
	}

	/**
	 * 通过name获取 Bean
	 * @param <T> Bean类型
	 * @param name Bean名称
	 * @return Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	/**
	 * 通过class获取Bean
	 * @param <T> Bean类型
	 * @param clazz Bean类
	 * @return Bean对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * @param <T> bean类型
	 * @param name Bean名称
	 * @param clazz bean类型
	 * @return Bean对象
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}

	/**
	 * 获取指定类型对应的所有Bean，包括子类
	 * @param <T> Bean类型
	 * @param type 类、接口，null表示获取所有bean
	 * @return 类型对应的bean，key是bean注册的name，value是Bean
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	/**
	 * 获取指定类型对应的Bean名称，包括子类
	 * @param type 类、接口，null表示获取所有bean名称
	 * @return bean名称
	 */
	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	/**
	 * 获取配置文件配置项的值
	 * @param key 配置项key
	 * @return 属性值
	 */
	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 * @return 当前的环境配置
	 */
	public static String[] getActiveProfiles() {
		return context.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取环境
	 */
	public static Environment getEnvironment() {
		return context.getEnvironment();
	}

	/**
	 * 获取{@link ApplicationContext}
	 * @return {@link ApplicationContext}
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * 发布事件 Spring 4.2+ 版本事件可以不再是{@link ApplicationEvent}的子类
	 * @param event 待发布的事件
	 */
	public static void publishEvent(Object event) {
		if (context != null) {
			context.publishEvent(event);
		}
	}

}
