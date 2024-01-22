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

package org.ballcat.log.operation.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballcat.common.util.JsonUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hccake
 */
@Slf4j
public abstract class AbstractOperationLogHandler<T> implements OperationLogHandler<T> {

	/**
	 * <p>
	 * 忽略记录的参数类型列表
	 * </p>
	 * 忽略判断时只针对方法入参类型，如果入参为对象，其某个属性需要忽略的无法处理，可以使用 @JsonIgnore 进行忽略。
	 */
	private final List<Class<?>> ignoredParamClasses = new ArrayList<>(
			Arrays.asList(ServletRequest.class, ServletResponse.class, MultipartFile.class));

	/**
	 * 添加忽略记录的参数类型
	 * @param clazz 参数类型
	 */
	public void addIgnoredParamClass(Class<?> clazz) {
		this.ignoredParamClasses.add(clazz);
	}

	/**
	 * 获取方法参数
	 * @param joinPoint 切点
	 * @return 当前方法入参的Json Str
	 */
	public String getParams(ProceedingJoinPoint joinPoint) {
		// 获取方法签名
		Signature signature = joinPoint.getSignature();
		String strClassName = joinPoint.getTarget().getClass().getName();
		String strMethodName = signature.getName();
		MethodSignature methodSignature = (MethodSignature) signature;
		log.debug("[getParams]，获取方法参数[类名]:{},[方法]:{}", strClassName, strMethodName);

		String[] parameterNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		if (ObjectUtils.isEmpty(parameterNames)) {
			return null;
		}
		Map<String, Object> paramsMap = new HashMap<>();
		for (int i = 0; i < parameterNames.length; i++) {
			Object arg = args[i];
			if (arg == null) {
				paramsMap.put(parameterNames[i], null);
				continue;
			}
			Class<?> argClass = arg.getClass();
			// 忽略部分类型的参数记录
			for (Class<?> ignoredParamClass : this.ignoredParamClasses) {
				if (ignoredParamClass.isAssignableFrom(argClass)) {
					arg = "ignored param type: " + argClass;
					break;
				}
			}
			paramsMap.put(parameterNames[i], arg);
		}

		String params = "";
		try {
			// 入参类中的属性可以通过注解进行数据落库脱敏以及忽略等操作
			params = JsonUtils.toJson(paramsMap);
		}
		catch (Exception e) {
			log.error("[getParams]，获取方法参数异常，[类名]:{},[方法]:{}", strClassName, strMethodName, e);
		}

		return params;
	}

}
