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

package org.ballcat.security.authorization;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.ballcat.security.access.expression.SecurityExpressionHandler;
import org.ballcat.security.annotation.Authorize;
import org.springframework.aop.support.AopUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.lang.NonNull;

/**
 * 默认的鉴权管理器
 *
 * @author Hccake
 * @since 2.0.0
 * @see org.springframework.security.authorization.AuthorizationManager
 */
public class DefaultAuthorizeManager implements AuthorizeManager {

	private final AuthorizeExpressionAttributeRegistry registry = new AuthorizeExpressionAttributeRegistry();

	private final SecurityExpressionHandler expressionHandler;

	public DefaultAuthorizeManager(SecurityExpressionHandler expressionHandler) {
		this.expressionHandler = expressionHandler;
	}

	@Override
	public boolean check(MethodInvocation mi) {
		ExpressionAttribute attribute = this.registry.getAttribute(mi);
		if (attribute == ExpressionAttribute.NULL_ATTRIBUTE) {
			return false;
		}
		EvaluationContext ctx = this.expressionHandler.createEvaluationContext(mi);
		return evaluateAsBoolean(attribute.getExpression(), ctx);
	}

	static boolean evaluateAsBoolean(Expression expr, EvaluationContext ctx) {
		try {
			return Boolean.TRUE.equals(expr.getValue(ctx, Boolean.class));
		}
		catch (EvaluationException ex) {
			throw new IllegalArgumentException("Failed to evaluate expression '" + expr.getExpressionString() + "'",
					ex);
		}
	}

	private final class AuthorizeExpressionAttributeRegistry
			extends AbstractExpressionAttributeRegistry<ExpressionAttribute> {

		@NonNull
		@Override
		ExpressionAttribute resolveAttribute(Method method, Class<?> targetClass) {
			Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
			Authorize authorize = findPreAuthorizeAnnotation(specificMethod);
			if (authorize == null) {
				return ExpressionAttribute.NULL_ATTRIBUTE;
			}
			Expression preAuthorizeExpression = DefaultAuthorizeManager.this.expressionHandler.getExpressionParser()
				.parseExpression(authorize.value());
			return new ExpressionAttribute(preAuthorizeExpression);
		}

		private Authorize findPreAuthorizeAnnotation(Method method) {
			Authorize authorize = AuthorizationAnnotationUtils.findUniqueAnnotation(method, Authorize.class);
			return (authorize != null) ? authorize
					: AuthorizationAnnotationUtils.findUniqueAnnotation(method.getDeclaringClass(), Authorize.class);
		}

	}

}
