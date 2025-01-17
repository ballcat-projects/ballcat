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

package org.ballcat.log.operation;

import lombok.EqualsAndHashCode;
import org.aopalliance.aop.Advice;
import org.ballcat.log.operation.annotation.OperationLog;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 操作日志注解切面。
 *
 * @author Hccake
 * @since 2.0.0
 */
@EqualsAndHashCode(callSuper = true)
public class OperationLogAnnotationAdvisor extends AbstractPointcutAdvisor {

	private final Advice advice;

	private final Pointcut pointcut;

	public OperationLogAnnotationAdvisor(OperationLogMethodInterceptor operationLogMethodInterceptor) {
		this.advice = operationLogMethodInterceptor;
		this.pointcut = buildPointcut();
	}

	protected Pointcut buildPointcut() {
		return new AnnotationMatchingPointcut(null, OperationLog.class, true);
	}

	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

}
