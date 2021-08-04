package com.hccake.ballcat.common.i18n;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

/**
 * 利用 ResponseBodyAdvice 对返回结果进行国际化处理
 *
 * @author Yakir
 * @author hccake
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class I18nResponseAdvice implements ResponseBodyAdvice<Object> {

	private final MessageSource messageSource;

	// todo 配置文件
	private Locale defaultLocal = Locale.SIMPLIFIED_CHINESE;

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

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		switchLanguage(body);
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
				// 获取国际化的唯一标识
				String annotationCode = i18nField.code();
				String code = StrUtil.isNotEmpty(annotationCode) ? annotationCode : (String) fieldValue;
				if (StrUtil.isEmpty(code)) {
					continue;
				}
				// 把当前 field 的值更新为国际化后的属性
				Locale locale = LocaleContextHolder.getLocale();
				String message;
				try {
					message = messageSource.getMessage(code, null, locale);
				}
				catch (NoSuchMessageException e) {
					log.warn("[switchLanguage]未找到对应的国际化配置，code: {}, local: {}.切换到默认的语言：{}", code, locale, defaultLocal);
					message = messageSource.getMessage(code, null, defaultLocal);
				}
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

}
