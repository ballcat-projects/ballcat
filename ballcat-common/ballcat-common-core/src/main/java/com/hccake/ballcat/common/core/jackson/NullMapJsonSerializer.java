package com.hccake.ballcat.common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 空 Map 序列化处理器 Map 为 null，则序列化为 {}
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 23:17
 */
public class NullMapJsonSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		if (value == null) {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeEndObject();
		}
		else {
			jsonGenerator.writeObject(value);
		}
	}

}
