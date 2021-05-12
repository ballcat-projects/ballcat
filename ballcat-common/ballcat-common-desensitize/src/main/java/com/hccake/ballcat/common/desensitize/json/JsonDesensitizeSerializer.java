package com.hccake.ballcat.common.desensitize.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hccake.ballcat.common.desensitize.AnnotationHandlerHolder;
import com.hccake.ballcat.common.desensitize.functions.DesensitizeFunction;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Jackson脱敏处理序列化器
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class JsonDesensitizeSerializer<T> extends JsonSerializer<T> {

	/**
	 * json 脱敏处理注解
	 */
	private final Annotation jsonDesensitizeAnnotation;

	/**
	 * 脱敏注解条件工具类
	 */
	private final DesensitizeStrategy desensitizeStrategy;

	public JsonDesensitizeSerializer(Annotation jsonDesensitizeAnnotation, DesensitizeStrategy desensitizeStrategy) {
		this.jsonDesensitizeAnnotation = jsonDesensitizeAnnotation;
		this.desensitizeStrategy = desensitizeStrategy;
	}

	@Override
	public void serialize(T value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		String str = (String) value;
		String fieldName = jsonGenerator.getOutputContext().getCurrentName();
		// 未开启脱敏
		if (desensitizeStrategy != null && desensitizeStrategy.ignoreField(fieldName)) {
			jsonGenerator.writeString(str);
			return;
		}
		DesensitizeFunction handleFunction = AnnotationHandlerHolder
				.getHandleFunction(jsonDesensitizeAnnotation.annotationType());
		if (handleFunction == null) {
			jsonGenerator.writeString(str);
			return;
		}
		jsonGenerator.writeString(handleFunction.desensitize(jsonDesensitizeAnnotation, str));
	}

}
