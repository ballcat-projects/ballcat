package com.hccake.ballcat.autoconfigure.log;

import com.hccake.ballcat.common.log.operation.aspect.OperationLogAspect;
import com.hccake.ballcat.common.log.operation.handler.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
public class OperationLogAutoConfiguration {

	/**
	 * 注册操作日志Aspect
	 * @return OperationLogAspect
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public <T> OperationLogAspect<T> operationLogAspect(OperationLogHandler<T> operationLogHandler) {
		return new OperationLogAspect<>(operationLogHandler);
	}

}
