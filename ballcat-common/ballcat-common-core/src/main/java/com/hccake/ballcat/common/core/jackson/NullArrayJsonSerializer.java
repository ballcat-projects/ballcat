package com.hccake.ballcat.common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 空数组序列化处理器 如果 Array 为 null，则序列化为 []
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 23:17
 */
public class NullArrayJsonSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		if (value == null) {
			jsonGenerator.writeStartArray();
			jsonGenerator.writeEndArray();
		}
		else {
			jsonGenerator.writeObject(value);
		}
	}

}
