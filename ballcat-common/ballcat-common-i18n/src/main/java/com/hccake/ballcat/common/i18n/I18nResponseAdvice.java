package com.hccake.ballcat.common.i18n;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 利用 ResponseBodyAdvice 对返回结果进行国际化处理
 *
 * @author Yakir
 * @author hccake
 */
@Slf4j
@RestControllerAdvice
public class I18nResponseAdvice implements ResponseBodyAdvice<Object> {

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
			fallbackLocale = new Locale(arr[0], arr[1]);
		}

		this.useCodeAsDefaultMessage = i18nOptions.isUseCodeAsDefaultMessage();
	}

	/**
	 * supports all type
	 * @param returnType MethodParameter
	 * @param converterType 消息转换器
	 * @return always true
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
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

		for (Field field : ReflectUtil.getFields(sourceClass)) {
			Class<?> fieldType = field.getType();
			Object fieldValue = ReflectUtil.getFieldValue(source, field);

			if (fieldValue instanceof String) {
				// 若不存在国际化注解 直接跳过
				I18nField i18nField = field.getAnnotation(I18nField.class);
				if (i18nField == null) {
					continue;
				}

				// 国际化条件判断
				String conditionExpression = i18nField.condition();
				if (StrUtil.isNotEmpty(conditionExpression)) {
					Expression expression = EXPRESSION_CACHE.get(conditionExpression);
					if (expression == null) {
						expression = PARSER.parseExpression(conditionExpression);
						EXPRESSION_CACHE.put(conditionExpression, expression);
					}
					Boolean needI18n = expression.getValue(source, Boolean.class);
					if (needI18n != null && !needI18n) {
						continue;
					}
				}

				// 获取国际化标识
				String code = parseMessageCode(source, sourceClass, field, (String) fieldValue, i18nField);
				if (StrUtil.isEmpty(code)) {
					continue;
				}

				// 把当前 field 的值更新为国际化后的属性
				Locale locale = LocaleContextHolder.getLocale();
				String message = codeToMessage(code, locale, fallbackLocale);
				ReflectUtil.setFieldValue(source, field, message);
			}
			else if (fieldValue instanceof Collection) {
				@SuppressWarnings("unchecked")
				Collection<Object> elements = (Collection<Object>) fieldValue;
				if (CollectionUtil.isEmpty(elements)) {
					continue;
				}
				// 集合属性 递归处理
				for (Object element : elements) {
					switchLanguage(element);
				}
			}
			else if (fieldType.isArray()) {
				Object[] elements = (Object[]) fieldValue;
				if (elements == null || elements.length == 0) {
					continue;
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
		}
	}

	/**
	 * <h2>根据指定规则解析 messageCode 值
	 * <p>
	 * 先解析出来 code 值，再拼接上前缀，成为完整的 messageCode。
	 * </p>
	 * <br>
	 * 解析 code 流程如下：
	 * <ol>
	 * <li>优先获取注解指定的 code 值
	 * <li>其次根据指定的属性获取 code
	 * <li>再次使用当前属性的值做为 code
	 * <ol/>
	 * @param source 源对象
	 * @param sourceClass 源对象类型
	 * @param field 属性
	 * @param fieldValue 属性值
	 * @param i18nField 国际化注解
	 * @return String 国际化 code
	 */
	private String parseMessageCode(Object source, Class<?> sourceClass, Field field, String fieldValue,
			I18nField i18nField) {
		// 给 code 添加固定的前缀
		String prefix = i18nField.prefix();

		// 1. 优先获取注解的属性
		String code = i18nField.code();
		if (StrUtil.isNotEmpty(code)) {
			return prefix + code;
		}

		// 2. 根据指定的属性获取 code
		String codeProperty = i18nField.codeProperty();
		if (StrUtil.isNotEmpty(codeProperty)) {
			Field codePropertyField = ReflectUtil.getField(sourceClass, codeProperty);
			if (codePropertyField != null) {
				Object codePropertyValue = ReflectUtil.getFieldValue(source, field);
				// 如果codePropertyValue为null不处理
				if (codePropertyValue != null) {
					if (codePropertyValue instanceof String) {
						code = (String) codePropertyValue;
					}
					else if (codePropertyValue instanceof Number) {
						code = codePropertyValue.toString();
					}
					else {
						log.warn("错误的 codeProperty， 类 {} 的属性 {} 不是 String or Number 类型", codeProperty, sourceClass);
					}
				}
			}
			else {
				log.warn("错误的 codeProperty，类 {} 的属性 {} 不存在", sourceClass, codeProperty);
			}
		}
		if (StrUtil.isNotEmpty(code)) {
			return prefix + code;
		}

		// 3. 使用当前属性的值做为 code
		if (StrUtil.isNotEmpty(fieldValue)) {
			return prefix + fieldValue;
		}

		log.warn("解析 messageCode 失败：类 {} ，属性 {}", codeProperty, sourceClass);
		return null;
	}

	/**
	 * 转换 code 为对应的国家的语言文本
	 * @param code 国际化唯一标识
	 * @param locale 当前地区
	 * @param fallbackLocale 回退语言
	 * @return 国际化 text，或者 code 本身
	 */
	private String codeToMessage(String code, Locale locale, Locale fallbackLocale) {
		String message;

		NoSuchMessageException noSuchMessageException;
		try {
			message = messageSource.getMessage(code, null, locale);
			return message;
		}
		catch (NoSuchMessageException e) {
			noSuchMessageException = e;
			log.warn("[codeToMessage]未找到对应的国际化配置，code: {}, local: {}", code, locale);
		}

		// 当配置了回退语言时，尝试回退
		if (fallbackLocale != null && locale != fallbackLocale) {
			try {
				message = messageSource.getMessage(code, null, fallbackLocale);
				return message;
			}
			catch (NoSuchMessageException e) {
				log.warn("[codeToMessage]期望语言和回退语言中皆未找到对应的国际化配置，code: {}, local: {}, fallbackLocale：{}", code, locale,
						fallbackLocale);
			}
		}

		if (useCodeAsDefaultMessage) {
			return code;
		}
		else {
			throw noSuchMessageException;
		}
	}

}
