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

package org.ballcat.common.util;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Hccake 2019/9/3 10:29
 */
@SuppressWarnings("SpellCheckingInspection")
public final class SpelUtils {

	private SpelUtils() {
	}

	/**
	 * SpEL 解析器
	 */
	public static final ExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 方法参数获取
	 */
	public static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 支持 #p0 参数索引的表达式解析
	 * @param rootObject 根对象, method 所在类的对象实例
	 * @param spelExpression spel 表达式
	 * @param method 目标方法
	 * @param args 方法入参
	 * @return 解析后的字符串
	 */
	public static String parseValueToString(Object rootObject, Method method, Object[] args, String spelExpression) {
		StandardEvaluationContext context = getSpelContext(rootObject, method, args);
		return parseValueToString(context, spelExpression);
	}

	/**
	 * 支持 #p0 参数索引的表达式解析
	 * @param rootObject 根对象, method 所在的对象
	 * @param method 目标方法
	 * @param args 方法实际入参
	 * @return StandardEvaluationContext spel 上下文
	 */
	public static StandardEvaluationContext getSpelContext(Object rootObject, Method method, Object[] args) {
		// spel 上下文
		StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args,
				PARAMETER_NAME_DISCOVERER);
		// 方法参数名数组
		String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
		// 把方法参数放入 spel 上下文中
		if (parameterNames != null && parameterNames.length > 0) {
			for (int i = 0; i < parameterNames.length; i++) {
				context.setVariable(parameterNames[i], args[i]);
			}
		}
		return context;
	}

	/**
	 * 解析 spel 表达式
	 * @param context spel 上下文
	 * @param spelExpression spel 表达式
	 * @return String 解析后的字符串
	 */
	public static String parseValueToString(StandardEvaluationContext context, String spelExpression) {
		return PARSER.parseExpression(spelExpression).getValue(context, String.class);
	}

	/**
	 * 解析 spel 表达式
	 * @param context spel 上下文
	 * @param spelExpression spel 表达式
	 * @return 解析后的 List<String>
	 */
	public static List<String> parseValueToStringList(StandardEvaluationContext context, String spelExpression) {
		return PARSER.parseExpression(spelExpression).getValue(context, List.class);
	}

}
