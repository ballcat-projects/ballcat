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

package org.ballcat.datascope.interceptor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.ballcat.datascope.annotation.DataPermission;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author hccake
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DataPermissionAnnotationAdvisor extends AbstractPointcutAdvisor {

	private final Advice advice;

	private final Pointcut pointcut;

	public DataPermissionAnnotationAdvisor() {
		this.advice = new DataPermissionAnnotationInterceptor();
		this.pointcut = buildPointcut();
	}

	protected Pointcut buildPointcut() {
		Pointcut cpc = new AnnotationMatchingPointcut(DataPermission.class, true);
		Pointcut mpc = new AnnotationMatchingPointcut(null, DataPermission.class, true);
		return new ComposablePointcut(cpc).union(mpc);
	}

}
