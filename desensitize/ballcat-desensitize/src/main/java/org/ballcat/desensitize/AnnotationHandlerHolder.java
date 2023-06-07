/*
 * Copyright 2023 the original author or authors.
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
package org.ballcat.desensitize;

import org.ballcat.desensitize.enums.RegexDesensitizationTypeEnum;
import org.ballcat.desensitize.enums.SlideDesensitizationTypeEnum;
import org.ballcat.desensitize.functions.DesensitizeFunction;
import org.ballcat.desensitize.handler.RegexDesensitizationHandler;
import org.ballcat.desensitize.handler.SimpleDesensitizationHandler;
import org.ballcat.desensitize.handler.SlideDesensitizationHandler;
import org.ballcat.desensitize.json.annotation.JsonRegexDesensitize;
import org.ballcat.desensitize.json.annotation.JsonSimpleDesensitize;
import org.ballcat.desensitize.json.annotation.JsonSlideDesensitize;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解处理方法 脱敏注解->处理逻辑
 *
 * @author Yakir
 */
public final class AnnotationHandlerHolder {

	private static final AnnotationHandlerHolder INSTANCE = new AnnotationHandlerHolder();

	/**
	 * 注解类型 处理函数映射
	 */
	private final Map<Class<? extends Annotation>, DesensitizeFunction> annotationHandlers;

	private AnnotationHandlerHolder() {
		annotationHandlers = new ConcurrentHashMap<>();

		annotationHandlers.put(JsonSimpleDesensitize.class, (annotation, value) -> {
			// Simple 类型处理
			JsonSimpleDesensitize an = (JsonSimpleDesensitize) annotation;
			Class<? extends SimpleDesensitizationHandler> handlerClass = an.handler();
			SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
				.getSimpleHandler(handlerClass);
			if (null == desensitizationHandler) {
				throw new IllegalArgumentException("SimpleDesensitizationHandler can not be Null");
			}
			return desensitizationHandler.handle(value);
		});

		annotationHandlers.put(JsonRegexDesensitize.class, (annotation, value) -> {
			// 正则类型脱敏处理
			JsonRegexDesensitize an = (JsonRegexDesensitize) annotation;
			RegexDesensitizationTypeEnum type = an.type();
			RegexDesensitizationHandler regexDesensitizationHandler = DesensitizationHandlerHolder
				.getRegexDesensitizationHandler();
			return RegexDesensitizationTypeEnum.CUSTOM.equals(type)
					? regexDesensitizationHandler.handle(value, an.regex(), an.replacement())
					: regexDesensitizationHandler.handle(value, type);
		});

		annotationHandlers.put(JsonSlideDesensitize.class, (annotation, value) -> {
			// 滑动类型脱敏处理
			JsonSlideDesensitize an = (JsonSlideDesensitize) annotation;
			SlideDesensitizationTypeEnum type = an.type();
			SlideDesensitizationHandler slideDesensitizationHandler = DesensitizationHandlerHolder
				.getSlideDesensitizationHandler();
			return SlideDesensitizationTypeEnum.CUSTOM.equals(type) ? slideDesensitizationHandler.handle(value,
					an.leftPlainTextLen(), an.rightPlainTextLen(), an.maskString())
					: slideDesensitizationHandler.handle(value, type);
		});
	}

	/**
	 * 得到注解类型处理函数
	 * @param annotationType {@code annotationType} 注解类型
	 * @return {@link DesensitizeFunction}脱敏处理函数|null
	 */
	public static DesensitizeFunction getHandleFunction(Class<? extends Annotation> annotationType) {
		return INSTANCE.annotationHandlers.get(annotationType);
	}

	/**
	 * 添加注解处理函数
	 * @param annotationType {@code annotationType}指定注解类型
	 * @param desensitizeFunction {@link DesensitizeFunction}指定脱敏处理函数
	 * @return 上一个key 对应的脱敏处理函数 | null
	 */
	public static DesensitizeFunction addHandleFunction(Class<? extends Annotation> annotationType,
			DesensitizeFunction desensitizeFunction) {
		if (null == annotationType) {
			throw new IllegalArgumentException("annotation cannot be null");
		}
		if (null == desensitizeFunction) {
			throw new IllegalArgumentException("desensitization function cannot be null");
		}
		// 加入注解处理映射
		return INSTANCE.annotationHandlers.put(annotationType, desensitizeFunction);
	}

	/**
	 * 得到当前支持的注解处理类
	 * @return {@code 注解处理类列表}
	 */
	public static Set<Class<? extends Annotation>> getAnnotationClasses() {
		return INSTANCE.annotationHandlers.keySet();
	}

}
