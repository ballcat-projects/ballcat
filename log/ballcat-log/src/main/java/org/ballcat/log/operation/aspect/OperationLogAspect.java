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

package org.ballcat.log.operation.aspect;

import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballcat.log.operation.annotation.OperationLogging;
import org.ballcat.log.operation.handler.OperationLogHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

/**
 * 操作日志切面类
 *
 * @author Hccake 2019/10/15 18:16
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class OperationLogAspect<T> {

	private final OperationLogHandler<T> operationLogHandler;

	@Around("execution(@(@org.ballcat.log.operation.annotation.OperationLogging *) * *(..)) "
			+ "|| @annotation(org.ballcat.log.operation.annotation.OperationLogging)")
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

		T operationLog = this.operationLogHandler.buildLog(operationLogging, joinPoint);

		Throwable throwable = null;
		Object result = null;
		try {
			result = joinPoint.proceed();
			return result;
		}
		catch (Throwable e) {
			throwable = e;
			throw e;
		}
		finally {
			// 是否保存响应内容
			boolean isSaveResult = operationLogging.recordResult();
			// 操作日志记录处理
			handleLog(joinPoint, startTime, operationLog, throwable, isSaveResult, result);
		}
	}

	private void handleLog(ProceedingJoinPoint joinPoint, long startTime, T operationLog, Throwable throwable,
			boolean isSaveResult, Object result) {
		try {
			// 结束时间
			long executionTime = System.currentTimeMillis() - startTime;
			// 记录执行信息
			this.operationLogHandler.recordExecutionInfo(operationLog, joinPoint, executionTime, throwable,
					isSaveResult, result);
			// 处理操作日志
			this.operationLogHandler.handleLog(operationLog);
		}
		catch (Exception e) {
			log.error("记录操作日志异常：{}", operationLog);
		}
	}

}
