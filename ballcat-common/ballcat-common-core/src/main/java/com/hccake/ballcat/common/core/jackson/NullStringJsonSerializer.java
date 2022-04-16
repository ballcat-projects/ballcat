package com.hccake.ballcat.common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.Serializable;

/**
 * jackson NULL值序列化为 ""
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:19
 */
public class NullStringJsonSerializer extends JsonSerializer<Object> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		jsonGenerator.writeString("");
	}

}
