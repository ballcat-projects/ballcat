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

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.ballcat.security.authorization.SecurityChecker;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 默认的鉴权表达式处理器
 *
 * @author Hccake
 * @see org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
 * @since 2.0.0
 */
public class DefaultSecurityExpressionHandler implements SecurityExpressionHandler, ApplicationContextAware {

	private ExpressionParser expressionParser = new SpelExpressionParser();

	private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	private BeanResolver beanResolver;

	private final SecurityChecker securityChecker;

	public DefaultSecurityExpressionHandler(SecurityChecker securityChecker) {
		this.securityChecker = securityChecker;
	}

	@Override
	public ExpressionParser getExpressionParser() {
		return this.expressionParser;
	}

	public void setExpressionParser(ExpressionParser expressionParser) {
		this.expressionParser = expressionParser;
	}

	@Override
	public final EvaluationContext createEvaluationContext(MethodInvocation mi) {
		StandardEvaluationContext ctx = createEvaluationContextInternal(mi, getParameterNameDiscoverer());
		ctx.setBeanResolver(this.beanResolver);
		ctx.setRootObject(this.securityChecker);
		return ctx;
	}

	private StandardEvaluationContext createEvaluationContextInternal(MethodInvocation mi,
			ParameterNameDiscoverer parameterNameDiscoverer) {
		return new MethodBasedEvaluationContext(mi.getThis(), getSpecificMethod(mi), mi.getArguments(),
				parameterNameDiscoverer);
	}

	private Method getSpecificMethod(MethodInvocation mi) {
		return AopUtils.getMostSpecificMethod(mi.getMethod(), AopProxyUtils.ultimateTargetClass(mi.getThis()));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.beanResolver = new BeanFactoryResolver(applicationContext);
	}

	/**
	 * Sets the {@link ParameterNameDiscoverer} to use. The default is
	 * {@link DefaultParameterNameDiscoverer}.
	 * @param parameterNameDiscoverer new parameterNameDiscoverer
	 */
	public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	/**
	 * @return The current {@link ParameterNameDiscoverer}
	 */
	protected ParameterNameDiscoverer getParameterNameDiscoverer() {
		return this.parameterNameDiscoverer;
	}

}
