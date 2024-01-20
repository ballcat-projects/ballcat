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

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ballcat.security.annotation.Authorize;
import org.ballcat.security.exception.AccessDeniedException;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.support.Pointcuts;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 针对 {@link Authorize} 鉴权注解的拦截器
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
public class AuthorizeInterceptor implements MethodInterceptor, PointcutAdvisor, AopInfrastructureBean {

	private final Pointcut pointcut = classOrMethodPointcuts();

	private final AuthorizeManager authorizeManager;

	public AuthorizeInterceptor(AuthorizeManager authorizeManager) {
		this.authorizeManager = authorizeManager;
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		attemptAuthorization(mi);
		return mi.proceed();
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this;
	}

	@Override
	public boolean isPerInstance() {
		return true;
	}

	private void attemptAuthorization(MethodInvocation mi) {
		if (log.isDebugEnabled()) {
			log.debug("Authorizing method invocation " + mi);
		}
		boolean granted = this.authorizeManager.check(mi);
		if (!granted) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to authorize " + mi + " with authorization manager " + this.authorizeManager);
			}
			throw new AccessDeniedException("Access Denied");
		}
		if (log.isDebugEnabled()) {
			log.debug("Authorized method invocation " + mi);
		}
	}

	private static Pointcut classOrMethodPointcuts() {
		return Pointcuts.union(new AnnotationMatchingPointcut(null, Authorize.class, true),
				new AnnotationMatchingPointcut(Authorize.class, true));
	}

}
