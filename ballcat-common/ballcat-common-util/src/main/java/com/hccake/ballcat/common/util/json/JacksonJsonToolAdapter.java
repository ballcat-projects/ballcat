package com.hccake.ballcat.common.util.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * @author lingting 2021/2/25 21:04
 */
public class JacksonJsonToolAdapter implements JsonTool {

	@Getter
	static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public void config(Consumer<ObjectMapper> consumer) {
		consumer.accept(mapper);
	}

	@SneakyThrows
	@Override
	public String toJson(Object obj) {
		return mapper.writeValueAsString(obj);
	}

	@SneakyThrows
	@Override
	public <T> T toObj(String json, Class<T> r) {
		return mapper.readValue(json, r);
	}

	@SneakyThrows
	@Override
	public <T> T toObj(String json, Type t) {
		return mapper.readValue(json, mapper.constructType(t));
	}

	@SneakyThrows
	@Override
	public <T> T toObj(String json, TypeReference<T> t) {
		return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<T>() {
			@Override
			public Type getType() {
				return t.getType();
			}
		});
	}

}
