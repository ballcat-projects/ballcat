package com.hccake.ballcat.common.desensitize.json;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hccake.ballcat.common.desensitize.AnnotationHandlerHolder;
import com.hccake.ballcat.common.desensitize.DesensitizationHandlerHolder;
import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.hccake.ballcat.common.desensitize.functions.DesensitizeFunction;
import com.hccake.ballcat.common.desensitize.handler.RegexDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.handler.SlideDesensitizationHandler;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSimpleDesensitize;
import com.hccake.ballcat.common.desensitize.json.annotation.JsonSlideDesensitize;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

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
	private Annotation jsonDesensitizeAnnotation;

	/**
	 * 脱敏注解条件工具类
	 */
	private DesensitizeHandler desensitizeHandler;

	public JsonDesensitizeSerializer(Annotation jsonDesensitizeAnnotation, DesensitizeHandler desensitizeHandler) {
		this.jsonDesensitizeAnnotation = jsonDesensitizeAnnotation;
		this.desensitizeHandler = desensitizeHandler;
	}

	public Map<Class<?>, Function<String, String>> functionMap = new HashMap<>();

	{

	}

	@Override
	public void serialize(T value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		String str = (String) value;
		String fieldName = jsonGenerator.getOutputContext().getCurrentName();
		// 未开启脱敏
		if (desensitizeHandler != null && desensitizeHandler.ignore(fieldName)) {
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
