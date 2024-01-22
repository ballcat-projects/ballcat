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

package org.ballcat.i18n;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 利用 ResponseBodyAdvice 对返回结果进行国际化处理
 *
 * @author Yakir
 * @author hccake
 */
@Slf4j
@RestControllerAdvice
public class I18nResponseAdvice implements ResponseBodyAdvice<Object> {

	private static final ReflectionUtils.FieldFilter WRITEABLE_FIELDS = (field -> !(Modifier
		.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())));

	private final MessageSource messageSource;

	private final boolean useCodeAsDefaultMessage;

	private Locale fallbackLocale = null;

	/**
	 * SpEL 解析器
	 */
	private static final ExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 表达式缓存
	 */
	private static final Map<String, Expression> EXPRESSION_CACHE = new HashMap<>();

	public I18nResponseAdvice(MessageSource messageSource, I18nOptions i18nOptions) {
		this.messageSource = messageSource;

		String fallbackLanguageTag = i18nOptions.getFallbackLanguageTag();
		if (fallbackLanguageTag != null) {
			String[] arr = fallbackLanguageTag.split("-");
			Assert.isTrue(arr.length == 2, "error fallbackLanguageTag!");
			this.fallbackLocale = new Locale(arr[0], arr[1]);
		}

		this.useCodeAsDefaultMessage = i18nOptions.isUseCodeAsDefaultMessage();
	}

	/**
	 * 对于使用了 @I18nIgnore 之外的所有接口进行增强处理
	 * @param returnType MethodParameter
	 * @param converterType 消息转换器
	 * @return boolean: true is support, false is ignored
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		AnnotatedElement annotatedElement = returnType.getAnnotatedElement();
		I18nIgnore i18nIgnore = AnnotationUtils.findAnnotation(annotatedElement, I18nIgnore.class);
		return i18nIgnore == null;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {

		try {
			switchLanguage(body);
		}
		catch (Exception ex) {
			log.error("[国际化]响应体国际化处理异常：{}", body);
		}

		return body;
	}

	/**
	 * <p>
	 * 对提供了 {@link I18nClass} 注解的类进行国际化处理，递归检查所有属性。
	 * </p>
	 * ps: 仅处理 String 类型，且注解了 {@link I18nField} 的属性
	 * @param source 当前待处理的对象
	 */
	public void switchLanguage(Object source) {
		if (source == null) {
			return;
		}
		Class<?> sourceClass = source.getClass();
		// 只对添加了 I18nClass 注解的类进行处理
		I18nClass i18nClass = sourceClass.getAnnotation(I18nClass.class);
		if (i18nClass == null) {
			return;
		}

		ReflectionUtils.doWithFields(sourceClass, (field) -> {
			ReflectionUtils.makeAccessible(field);
			Class<?> fieldType = field.getType();
			Object fieldValue = ReflectionUtils.getField(field, source);

			if (fieldValue instanceof String) {
				// 若不存在国际化注解 直接跳过
				I18nField i18nField = field.getAnnotation(I18nField.class);
				if (i18nField == null) {
					return;
				}

				// 国际化条件判断
				String conditionExpression = i18nField.condition();
				if (StringUtils.hasText(conditionExpression)) {
					Expression expression = EXPRESSION_CACHE.computeIfAbsent(conditionExpression,
							PARSER::parseExpression);
					Boolean needI18n = expression.getValue(source, Boolean.class);
					if (needI18n != null && !needI18n) {
						return;
					}
				}

				// 获取国际化标识
				String code = parseMessageCode(source, (String) fieldValue, i18nField);
				if (!StringUtils.hasLength(code)) {
					return;
				}

				// 把当前 field 的值更新为国际化后的属性
				Locale locale = LocaleContextHolder.getLocale();
				String message = codeToMessage(code, locale, (String) fieldValue, this.fallbackLocale);
				ReflectionUtils.setField(field, source, message);
			}
			else if (fieldValue instanceof Collection) {
				@SuppressWarnings("unchecked")
				Collection<Object> elements = (Collection<Object>) fieldValue;
				if (CollectionUtils.isEmpty(elements)) {
					return;
				}
				// 集合属性 递归处理
				for (Object element : elements) {
					switchLanguage(element);
				}
			}
			else if (fieldType.isArray()) {
				Object[] elements = (Object[]) fieldValue;
				if (elements == null || elements.length == 0) {
					return;
				}
				// 数组 递归处理
				for (Object element : elements) {
					switchLanguage(element);
				}
			}
			else {
				// 其他类型的属性，递归判断处理
				switchLanguage(fieldValue);
			}
		}, WRITEABLE_FIELDS);
	}

	/**
	 * 解析获取国际化code
	 * <ul>
	 * <li>如果 @I18nField 注解中未指定 code 的 SpEL 表达式， 则使用当前属性值作为 code。
	 * <li>否则使用该表达式解析出来的 code 值。
	 * </ul>
	 * @param source 源对象
	 * @param fieldValue 属性值
	 * @param i18nField 国际化注解
	 * @return String 国际化 code
	 */
	private String parseMessageCode(Object source, String fieldValue, I18nField i18nField) {
		// 如果没有指定 spel，则直接返回属性值
		String codeExpression = i18nField.code();
		if (!StringUtils.hasText(codeExpression)) {
			return fieldValue;
		}

		// 否则解析 spel
		Expression expression = EXPRESSION_CACHE.computeIfAbsent(codeExpression, PARSER::parseExpression);
		return expression.getValue(source, String.class);
	}

	/**
	 * 转换 code 为对应的国家的语言文本
	 * @param code 国际化唯一标识
	 * @param locale 当前地区
	 * @param fallbackLocale 回退语言
	 * @return 国际化 text，或者 code 本身
	 */
	private String codeToMessage(String code, Locale locale, String defaultMessage, Locale fallbackLocale) {
		String message;

		try {
			message = this.messageSource.getMessage(code, null, locale);
			return message;
		}
		catch (NoSuchMessageException e) {
			log.warn("[codeToMessage]未找到对应的国际化配置，code: {}, local: {}", code, locale);
		}

		// 当配置了回退语言时，尝试回退
		if (fallbackLocale != null && locale != fallbackLocale) {
			try {
				message = this.messageSource.getMessage(code, null, fallbackLocale);
				return message;
			}
			catch (NoSuchMessageException e) {
				log.warn("[codeToMessage]期望语言和回退语言中皆未找到对应的国际化配置，code: {}, local: {}, fallbackLocale：{}", code, locale,
						fallbackLocale);
			}
		}

		if (this.useCodeAsDefaultMessage) {
			return code;
		}
		else {
			return defaultMessage;
		}
	}

}
