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
package com.hccake.ballcat.common.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * springFox 与 springboot 2.6.x 不兼容问题的处理
 * <p>
 * <a href="https://github.com/springfox/springfox/issues/3462">相关 issues 地址</a>
 *
 * @author hccake
 */
@Component
public class SpringfoxHandlerProviderBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
			customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
		}
		return bean;
	}

	private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
		List<T> copy = mappings.stream()
			.filter(mapping -> mapping.getPatternParser() == null)
			.collect(Collectors.toList());
		mappings.clear();
		mappings.addAll(copy);
	}

	@SuppressWarnings("unchecked")
	private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
		try {
			Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
			field.setAccessible(true);
			return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
