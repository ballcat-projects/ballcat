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

package org.ballcat.desensitize.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.desensitize.DesensitizationHandlerHolder;
import org.ballcat.desensitize.handler.IndexDesensitizationHandler;
import org.ballcat.desensitize.handler.RegexDesensitizationHandler;
import org.ballcat.desensitize.handler.SimpleDesensitizationHandler;
import org.ballcat.desensitize.handler.SlideDesensitizationHandler;
import org.ballcat.desensitize.rule.RuleClassReflectionException;
import org.ballcat.desensitize.rule.regex.NoneRegexDesensitizeRule;
import org.ballcat.desensitize.rule.regex.RegexDesensitizeRule;
import org.ballcat.desensitize.rule.slide.NoneSlideDesensitizeRule;
import org.ballcat.desensitize.rule.slide.SlideDesensitizeRule;

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
	private final Map<Class<? extends Annotation>, AnnotationDesensitizeFunction> annotationHandlers;

	private AnnotationHandlerHolder() {
		this.annotationHandlers = new ConcurrentHashMap<>();

		this.annotationHandlers.put(SimpleDesensitize.class, (annotation, value) -> {
			// Simple 类型处理
			SimpleDesensitize an = (SimpleDesensitize) annotation;
			Class<? extends SimpleDesensitizationHandler> handlerClass = an.handler();
			SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
				.getSimpleDesensitizationHandler(handlerClass);
			if (null == desensitizationHandler) {
				throw new IllegalArgumentException("SimpleDesensitizationHandler can not be Null");
			}
			return desensitizationHandler.mask(value);
		});

		this.annotationHandlers.put(RegexDesensitize.class, (annotation, value) -> {
			// 正则类型脱敏处理
			RegexDesensitize regexAnnotation = (RegexDesensitize) annotation;
			Class<? extends RegexDesensitizeRule> ruleClass = regexAnnotation.rule();
			RegexDesensitizationHandler regexDesensitizationHandler = DesensitizationHandlerHolder
				.getRegexDesensitizationHandler();
			if (ruleClass.equals(NoneRegexDesensitizeRule.class)) {
				return regexDesensitizationHandler.mask(value, regexAnnotation.regex(), regexAnnotation.replacement());
			}
			else {
				RegexDesensitizeRule regexDesensitizeRule = newInstance(ruleClass);
				return regexDesensitizationHandler.mask(value, regexDesensitizeRule.getRegex(),
						regexDesensitizeRule.getReplacement());
			}

		});

		this.annotationHandlers.put(SlideDesensitize.class, (annotation, value) -> {
			// 滑动类型脱敏处理
			SlideDesensitize slideAnnotation = (SlideDesensitize) annotation;
			Class<? extends SlideDesensitizeRule> ruleClass = slideAnnotation.rule();
			SlideDesensitizationHandler slideDesensitizationHandler = DesensitizationHandlerHolder
				.getSlideDesensitizationHandler();
			if (ruleClass.equals(NoneSlideDesensitizeRule.class)) {
				return slideDesensitizationHandler.mask(value, slideAnnotation.leftPlainTextLen(),
						slideAnnotation.rightPlainTextLen(), slideAnnotation.maskString(), slideAnnotation.reverse());
			}
			else {
				SlideDesensitizeRule slideDesensitizeRule = newInstance(ruleClass);
				return slideDesensitizationHandler.mask(value, slideDesensitizeRule.leftPlainTextLen(),
						slideDesensitizeRule.rightPlainTextLen(), slideDesensitizeRule.maskString(),
						slideDesensitizeRule.reverse());
			}
		});

		this.annotationHandlers.put(IndexDesensitize.class, (annotation, value) -> {
			// 规则类型脱敏处理
			IndexDesensitize an = (IndexDesensitize) annotation;
			IndexDesensitizationHandler indexDesensitizationHandler = DesensitizationHandlerHolder
				.getIndexDesensitizationHandler();
			return indexDesensitizationHandler.mask(value, an.maskCharacter(), an.reverse(), an.rule());
		});
	}

	/**
	 * 得到注解类型处理函数
	 * @param annotationType {@code annotationType} 注解类型
	 * @return {@link AnnotationDesensitizeFunction}脱敏处理函数|null
	 */
	public static AnnotationDesensitizeFunction getHandleFunction(Class<? extends Annotation> annotationType) {
		return INSTANCE.annotationHandlers.get(annotationType);
	}

	/**
	 * 添加注解处理函数
	 * @param annotationType {@code annotationType}指定注解类型
	 * @param annotationDesensitizeFunction {@link AnnotationDesensitizeFunction}指定脱敏处理函数
	 * @return 上一个key 对应的脱敏处理函数 | null
	 */
	public static AnnotationDesensitizeFunction addHandleFunction(Class<? extends Annotation> annotationType,
			AnnotationDesensitizeFunction annotationDesensitizeFunction) {
		if (null == annotationType) {
			throw new IllegalArgumentException("annotation cannot be null");
		}
		if (null == annotationDesensitizeFunction) {
			throw new IllegalArgumentException("desensitization function cannot be null");
		}
		// 加入注解处理映射
		return INSTANCE.annotationHandlers.put(annotationType, annotationDesensitizeFunction);
	}

	/**
	 * 得到当前支持的注解处理类
	 * @return {@code 注解处理类列表}
	 */
	public static Set<Class<? extends Annotation>> getAnnotationClasses() {
		return INSTANCE.annotationHandlers.keySet();
	}

	private static <T> T newInstance(Class<T> clazz) {
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		}
		catch (NoSuchMethodException e) {
			throw new RuleClassReflectionException("No default constructor found in class " + clazz.getName(), e);
		}
		catch (IllegalAccessException e) {
			throw new RuleClassReflectionException("Cannot access constructor of class " + clazz.getName(), e);
		}
		catch (InstantiationException e) {
			throw new RuleClassReflectionException("Cannot instantiate object of class " + clazz.getName(), e);
		}
		catch (InvocationTargetException e) {
			throw new RuleClassReflectionException("Constructor threw an exception for class " + clazz.getName(), e);
		}
	}

}
