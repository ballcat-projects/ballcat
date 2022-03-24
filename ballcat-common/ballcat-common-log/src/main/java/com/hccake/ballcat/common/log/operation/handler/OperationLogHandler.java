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
	 * 创建操作日志
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 当前执行方法的切点信息
	 * @return T 操作日志对象
	 */
	T buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint);

	/**
	 * 目标方法执行完成后进行信息补充记录, 如执行时长，异常信息，还可以通过切点记录返回值，如果需要的话
	 * @param log 操作日志对象 {@link #buildLog}
	 * @param joinPoint 当前执行方法的切点信息
	 * @param executionTime 方法执行时长
	 * @param throwable 方法执行的异常，为 null 则表示无异常
	 * @param isSaveResult 是否记录返回值
	 * @param result 方法执行的返回值
	 * @return T 操作日志对象
	 */
	T recordExecutionInfo(T log, ProceedingJoinPoint joinPoint, long executionTime, Throwable throwable, boolean isSaveResult, Object result);

	/**
	 * 处理日志，可以在这里进行存储，或者输出
	 * @param operationLog 操作日志
	 */
	void handleLog(T operationLog);

}
