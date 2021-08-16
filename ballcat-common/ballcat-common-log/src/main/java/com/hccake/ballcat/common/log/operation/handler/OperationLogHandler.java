package com.hccake.ballcat.common.log.operation.handler;

import com.hccake.ballcat.common.log.operation.annotation.OperationLogging;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 操作日志业务类
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 19:57
 */
public interface OperationLogHandler<T> {

	/**
	 * 处理日志
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 当前执行方法的切点信息
	 * @param executionTime 方法执行时长
	 * @param throwable 方法执行的异常，为 null 则表示无异常
	 */
	default void handleLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint, long executionTime,
			Throwable throwable) {
		T log = buildLog(operationLogging, joinPoint, executionTime, throwable);
		saveLog(log);
	}

	/**
	 * 创建操作日志
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 当前执行方法的切点信息
	 * @param executionTime 方法执行时长
	 * @param throwable 方法执行的异常，为 null 则表示无异常
	 * @return T 操作日志对象
	 */
	T buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint, long executionTime,
			Throwable throwable);

	/**
	 * 保存操作日志
	 * @param operationLog 操作日志
	 */
	void saveLog(T operationLog);

}
