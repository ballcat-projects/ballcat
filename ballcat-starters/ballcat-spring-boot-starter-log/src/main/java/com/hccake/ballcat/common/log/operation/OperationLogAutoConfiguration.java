package com.hccake.ballcat.common.log.operation;

import com.hccake.ballcat.common.log.operation.aspect.OperationLogAspect;
import com.hccake.ballcat.common.log.operation.event.OperationLogListener;
import com.hccake.ballcat.common.log.operation.service.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
public class OperationLogAutoConfiguration {

	/**
	 * 注册日志保存事件监听器
	 * @return OperationLogListener
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public OperationLogListener operationLogListener(OperationLogHandler operationLogHandler) {
		return new OperationLogListener(operationLogHandler);
	}

	/**
	 * 注册操作日志Aspect
	 * @return OperationLogAspect
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public OperationLogAspect operationLogAspect(ApplicationEventPublisher publisher) {
		return new OperationLogAspect(publisher);
	}

}
