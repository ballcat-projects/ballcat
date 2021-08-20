package com.hccake.ballcat.common.log.operation.aspect;

import com.hccake.ballcat.common.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.common.log.operation.handler.OperationLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:16 操作日志切面类
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class OperationLogAspect<T> {

	private final OperationLogHandler<T> operationLogHandler;

	@Around("execution(@(@com.hccake.ballcat.common.log.operation.annotation.OperationLogging *) * *(..)) "
			+ "|| @annotation(com.hccake.ballcat.common.log.operation.annotation.OperationLogging)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		// 开始时间
		long startTime = System.currentTimeMillis();

		// 获取目标方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		// 获取操作日志注解 备注： AnnotationUtils.findAnnotation 方法无法获取继承的属性
		OperationLogging operationLogging = AnnotatedElementUtils.findMergedAnnotation(method, OperationLogging.class);

		// 获取操作日志 DTO
		Assert.notNull(operationLogging, "operationLogging annotation must not be null!");

		T operationLog = operationLogHandler.buildLog(operationLogging, joinPoint);

		Throwable throwable = null;
		try {
			return joinPoint.proceed();
		}
		catch (Throwable e) {
			throwable = e;
			throw e;
		}
		finally {
			// 操作日志记录处理
			handleLog(joinPoint, startTime, operationLog, throwable);
		}
	}

	private void handleLog(ProceedingJoinPoint joinPoint, long startTime, T operationLog, Throwable throwable) {
		try {
			// 结束时间
			long executionTime = System.currentTimeMillis() - startTime;
			// 记录执行信息
			operationLogHandler.recordExecutionInfo(operationLog, joinPoint, executionTime, throwable);
			// 处理操作日志
			operationLogHandler.handleLog(operationLog);
		}
		catch (Exception e) {
			log.error("记录操作日志异常：{}", operationLog);
		}
	}

}
