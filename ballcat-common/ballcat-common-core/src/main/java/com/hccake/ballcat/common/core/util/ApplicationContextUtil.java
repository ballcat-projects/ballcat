package com.hccake.ballcat.common.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2020/6/12 16:36
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * 获取环境
	 *
	 * @author lingting 2020-06-12 16:38:56
	 */
	public Environment getEnvironment() {
		return context.getEnvironment();
	}

	public ApplicationContext getContext() {
		return context;
	}

}
