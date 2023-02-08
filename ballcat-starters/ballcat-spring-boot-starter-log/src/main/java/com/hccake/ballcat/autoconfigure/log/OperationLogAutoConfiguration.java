package com.hccake.ballcat.autoconfigure.log;

import com.hccake.ballcat.autoconfigure.log.properties.OperationLogProperties;
import com.hccake.ballcat.common.log.operation.aspect.OperationLogAspect;
import com.hccake.ballcat.common.log.operation.handler.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Hccake 2019/10/15 18:20
 */
@EnableConfigurationProperties(OperationLogProperties.class)
@ConditionalOnProperty(prefix = OperationLogProperties.PREFIX, name = "enabled", matchIfMissing = true,
		havingValue = "true")
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
