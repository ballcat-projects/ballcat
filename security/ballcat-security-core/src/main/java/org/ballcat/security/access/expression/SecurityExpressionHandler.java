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

package org.ballcat.security.access.expression;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 安全表达式处理器
 *
 * @author Hccake
 * @since 2.0.0
 * @see org.springframework.security.access.expression.SecurityExpressionHandler
 */
public interface SecurityExpressionHandler extends AopInfrastructureBean {

	/**
	 * @return an expression parser for the expressions used by the implementation.
	 */
	ExpressionParser getExpressionParser();

	/**
	 * Provides an evaluation context in which to evaluate security expressions for the
	 * invocation type.
	 */
	EvaluationContext createEvaluationContext(MethodInvocation invocation);

}
