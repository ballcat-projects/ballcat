package com.hccake.ballcat.common.core.spring.compose;

import com.hccake.ballcat.common.core.compose.ContextComponent;
import com.hccake.ballcat.common.core.spring.BallcatBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2022/10/22 15:10
 */
@Component
public class ContextComposeBeanPostProcessor implements BallcatBeanPostProcessor {

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return bean != null && ContextComponent.class.isAssignableFrom(bean.getClass());
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		((ContextComponent) bean).onApplicationStart();
		return bean;
	}

}
