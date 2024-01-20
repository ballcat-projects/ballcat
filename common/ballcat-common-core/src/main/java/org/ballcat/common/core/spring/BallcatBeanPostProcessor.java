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

package org.ballcat.common.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * @author lingting 2022/10/22 15:11
 */
public interface BallcatBeanPostProcessor extends BeanPostProcessor, Ordered {

	/**
	 * 判断是否处理该bean
	 * @param bean bean实例
	 * @param beanName bean名称
	 * @param isBefore true表示当前为初始化之前是否处理判断, false 表示当前为初始化之后是否处理判断
	 * @return boolean true 表示要处理该bean
	 */
	boolean isProcess(Object bean, String beanName, boolean isBefore);

	/**
	 * 初始化之前处理
	 * @param bean bean
	 * @param beanName bean名称
	 * @return java.lang.Object
	 */
	default Object postProcessBefore(Object bean, String beanName) {
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	/**
	 * 初始化之后处理
	 * @param bean bean
	 * @param beanName bean名称
	 * @return java.lang.Object
	 */
	default Object postProcessAfter(Object bean, String beanName) {
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

	@Override
	default int getOrder() {
		// 默认优先级
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (isProcess(bean, beanName, true)) {
			return postProcessBefore(bean, beanName);
		}

		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (isProcess(bean, beanName, false)) {
			return postProcessAfter(bean, beanName);
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

}
