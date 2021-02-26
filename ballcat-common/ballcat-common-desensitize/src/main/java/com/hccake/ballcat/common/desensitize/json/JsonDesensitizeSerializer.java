package com.hccake.ballcat.common.desensitize.json;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hccake.ballcat.common.desensitize.DesensitizationHandlerHolder;
import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.handler.RegexDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SlideDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSimpleDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSlideDesensitize;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Jackson脱敏处理序列化器
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class JsonDesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {

	/**
	 * json 脱敏处理注解
	 */
	private Annotation jsonDesensitizeAnnotation;

	/**
	 * 脱敏注解Class列表
	 */
	private final List<Class<? extends Annotation>> jsonDesensitizeAnnotationClasses = Arrays
			.asList(JsonSimpleDesensitize.class, JsonRegexDesensitize.class, JsonSlideDesensitize.class);

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers)
			throws IOException {
		String str = value;
		if (jsonDesensitizeAnnotation instanceof JsonSimpleDesensitize) {
			// Simple 类型处理
			JsonSimpleDesensitize an = (JsonSimpleDesensitize) this.jsonDesensitizeAnnotation;
			Class<? extends SimpleDesensitizationHandler> handlerClass = an.handler();
			SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
					.getSimpleHandler(handlerClass);
			Assert.notNull(desensitizationHandler, "SimpleDesensitizationHandler can not be Null");
			str = desensitizationHandler.handle(value);
		}
		else if (jsonDesensitizeAnnotation instanceof JsonRegexDesensitize) {
			// 正则类型脱敏处理
			JsonRegexDesensitize an = (JsonRegexDesensitize) this.jsonDesensitizeAnnotation;
			RegexDesensitizationTypeEnum type = an.type();
			RegexDesensitizationHandler regexDesensitizationHandler = DesensitizationHandlerHolder
					.getRegexDesensitizationHandler();
			str = RegexDesensitizationTypeEnum.CUSTOM.equals(type)
					? regexDesensitizationHandler.handle(value, an.regex(), an.replacement())
					: regexDesensitizationHandler.handle(value, type);
		}
		else if (jsonDesensitizeAnnotation instanceof JsonSlideDesensitize) {
			// 滑动类型脱敏处理
			JsonSlideDesensitize an = (JsonSlideDesensitize) this.jsonDesensitizeAnnotation;
			SlideDesensitizationTypeEnum type = an.type();
			SlideDesensitizationHandler slideDesensitizationHandler = DesensitizationHandlerHolder
					.getSlideDesensitizationHandler();
			str = SlideDesensitizationTypeEnum.CUSTOM.equals(type) ? slideDesensitizationHandler.handle(value,
					an.leftPlainTextLen(), an.rightPlainTextLen(), an.maskString())
					: slideDesensitizationHandler.handle(value, type);
		}
		jsonGenerator.writeString(str);
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		if (beanProperty == null) {
			return serializerProvider.findNullValueSerializer(null);
		}
		if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
			for (Class<? extends Annotation> annotationClass : jsonDesensitizeAnnotationClasses) {
				Annotation annotation = getJsonDesensitizeAnnotation(beanProperty, annotationClass);
				if (annotation != null) {
					this.jsonDesensitizeAnnotation = annotation;
					return this;
				}
			}
		}
		return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
	}

	/**
	 * 根据注解Class查找BeanProperty上的指定注解
	 * @param beanProperty beanProperty
	 * @param cls 注解Class
	 * @param <A> 注解类型
	 * @return 注解
	 */
	private <A extends Annotation> A getJsonDesensitizeAnnotation(BeanProperty beanProperty, Class<A> cls) {
		A jsonSimpleDesensitize = beanProperty.getAnnotation(cls);
		if (jsonSimpleDesensitize == null) {
			jsonSimpleDesensitize = beanProperty.getContextAnnotation(cls);
		}
		return jsonSimpleDesensitize;
	}

}
