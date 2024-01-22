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

package org.ballcat.common.core.strategy;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 用于获取Spring上下文中的策略实例，并解析该实例所实现的策略类型。
 *
 * @author kevin
 */
@Component
public class StrategyBeanProcessor implements BeanPostProcessor {

	private static final Class<StrategyComponent> STRATEGY_INSTANCE_CLASS = StrategyComponent.class;

	private static final Class<StrategyInterface> MARK_STRATEGY_INTERFACE_CLASS = StrategyInterface.class;

	@Override
	public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		Class<?> clazz = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
		StrategyComponent markStrategy = AnnotationUtils.findAnnotation(clazz, STRATEGY_INSTANCE_CLASS);
		if (markStrategy == null) {
			return bean;
		}
		for (Class<?> parent = clazz; parent != null; parent = parent.getSuperclass()) {
			for (Class<?> parentInterface : parent.getInterfaces()) {
				if (parentInterface.isAnnotationPresent(MARK_STRATEGY_INTERFACE_CLASS)) {
					String[] strategyValues = markStrategy.value();
					for (String strategyValue : strategyValues) {
						StrategyFactory.getStrategy(parentInterface, strategyValue).ifPresent(strategy -> {
							throw new BeanCreationException(String.format("策略冲突. 策略名称: %s, 策略类: %s, 冲突类: %s",
									strategyValue, strategy.getClass().getName(), bean.getClass().getName()));
						});
						StrategyFactory.addStrategy(parentInterface, strategyValue, bean);
					}
				}
			}
		}
		return bean;
	}

}
