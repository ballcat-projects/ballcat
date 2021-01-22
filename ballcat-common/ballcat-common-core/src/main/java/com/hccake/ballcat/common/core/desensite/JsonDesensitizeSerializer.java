package com.hccake.ballcat.common.core.desensite;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * Jackson脱敏处理序列化器
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class JsonDesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {

	private JsonDesensitize jsonDesensitize;

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializers)
			throws IOException {
		String type = jsonDesensitize.type();
		DesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder.TYPE_MAPS.get(type);
		Assert.notNull(desensitizationHandler, "DesensitizationHandler can not be Null");
		jsonGenerator.writeString(desensitizationHandler.handle(value));
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		if (beanProperty != null) {
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				JsonDesensitize jsonDesensitize = beanProperty.getAnnotation(JsonDesensitize.class);
				if (jsonDesensitize == null) {
					jsonDesensitize = beanProperty.getContextAnnotation(JsonDesensitize.class);
				}
				if (jsonDesensitize != null) {
					this.jsonDesensitize = jsonDesensitize;
					return this;
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}

}
