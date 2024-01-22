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

package org.ballcat.idempotent.key.generator;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballcat.common.util.SpelUtils;
import org.ballcat.idempotent.annotation.Idempotent;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 默认幂等key生成器
 *
 * @author lishangbu 2022/10/18
 */
public class DefaultIdempotentKeyGenerator implements IdempotentKeyGenerator {

	/**
	 * 生成幂等 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @return String 幂等标识
	 */
	@NonNull
	@Override
	public String generate(JoinPoint joinPoint, Idempotent idempotentAnnotation) {
		String uniqueExpression = idempotentAnnotation.uniqueExpression();
		// 如果没有填写表达式，直接返回 prefix
		if ("".equals(uniqueExpression)) {
			return idempotentAnnotation.prefix();
		}

		// 获取当前方法以及方法参数
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		// 根据当前切点，获取到 spEL 上下文
		StandardEvaluationContext spelContext = SpelUtils.getSpelContext(joinPoint.getTarget(), method, args);
		// 如果在 servlet 环境下，则将 request 信息放入上下文，便于获取请求参数
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes();
		if (requestAttributes != null) {
			spelContext.setVariable(RequestAttributes.REFERENCE_REQUEST, requestAttributes.getRequest());
		}
		// 解析出唯一标识
		String uniqueStr = SpelUtils.parseValueToString(spelContext, uniqueExpression);
		// 和 prefix 拼接获得完整的 key
		return idempotentAnnotation.prefix() + ":" + uniqueStr;
	}

}
