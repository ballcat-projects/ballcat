package com.hccake.ballcat.common.core.spring;

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
