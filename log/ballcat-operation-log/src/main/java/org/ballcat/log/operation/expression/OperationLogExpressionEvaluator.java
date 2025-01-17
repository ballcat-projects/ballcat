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

package org.ballcat.log.operation.expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.log.operation.OperationLogContextHolder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * 操作日志表达式解析器.
 *
 * @see CachedExpressionEvaluator
 * @author Hccake
 * @since 2.0.0
 */
public class OperationLogExpressionEvaluator {

	private final SpelExpressionParser parser;

	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

	/**
	 * Create a new instance with the specified {@link SpelExpressionParser}.
	 */
	public OperationLogExpressionEvaluator(SpelExpressionParser parser) {
		Assert.notNull(parser, "SpelExpressionParser must not be null");
		this.parser = parser;
	}

	/**
	 * Create a new instance with a default {@link SpelExpressionParser}.
	 */
	public OperationLogExpressionEvaluator() {
		this(new SpelExpressionParser());
	}

	/**
	 * Return the {@link SpelExpressionParser} to use.
	 */
	protected SpelExpressionParser getParser() {
		return this.parser;
	}

	/**
	 * Return a shared parameter name discoverer which caches data internally.
	 * @since 4.3
	 */
	protected ParameterNameDiscoverer getParameterNameDiscoverer() {
		return this.parameterNameDiscoverer;
	}

	/**
	 * Return the {@link Expression} for the specified SpEL value
	 * <p>
	 * Parse the expression if it hasn't been already.
	 * @param cache the cache to use
	 * @param elementKey the element on which the expression is defined
	 * @param expression the expression to parse
	 */
	protected Expression getExpression(Map<ExpressionKey, Expression> cache, AnnotatedElementKey elementKey,
			String expression) {

		ExpressionKey expressionKey = createKey(elementKey, expression);
		Expression expr = cache.get(expressionKey);
		if (expr == null) {
			expr = getParser().parseExpression(expression, ParserContext.TEMPLATE_EXPRESSION);
			cache.put(expressionKey, expr);
		}
		return expr;
	}

	private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
		return new ExpressionKey(elementKey, expression);
	}

	public EvaluationContext createEvaluationContext(Object target, Method targetMethod, Object[] args,
			BeanFactory beanFactory) {
		MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(target, targetMethod, args,
				getParameterNameDiscoverer());
		if (beanFactory != null) {
			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}

		// 变量优先级：自定义本地变量 > 自定义全局变量 > 方法入参
		Map<String, Object> globalVariables = OperationLogContextHolder.getGlobalVariables();
		evaluationContext.setVariables(globalVariables);
		Map<String, Object> localVariables = OperationLogContextHolder.getLocalVariables();
		evaluationContext.setVariables(localVariables);

		return evaluationContext;
	}

	public Object parseExpression(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return getExpression(this.expressionCache, methodKey, keyExpression).getValue(evalContext);
	}

	/**
	 * An expression key.
	 */
	protected static class ExpressionKey implements Comparable<ExpressionKey> {

		private final AnnotatedElementKey element;

		private final String expression;

		protected ExpressionKey(AnnotatedElementKey element, String expression) {
			Assert.notNull(element, "AnnotatedElementKey must not be null");
			Assert.notNull(expression, "Expression must not be null");
			this.element = element;
			this.expression = expression;
		}

		@Override
		public boolean equals(@Nullable Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof ExpressionKey)) {
				return false;
			}
			ExpressionKey otherKey = (ExpressionKey) other;
			return (this.element.equals(otherKey.element)
					&& ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
		}

		@Override
		public int hashCode() {
			return this.element.hashCode() * 29 + this.expression.hashCode();
		}

		@Override
		public String toString() {
			return this.element + " with expression \"" + this.expression + "\"";
		}

		@Override
		public int compareTo(ExpressionKey other) {
			int result = this.element.toString().compareTo(other.element.toString());
			if (result == 0) {
				result = this.expression.compareTo(other.expression);
			}
			return result;
		}

	}

}
