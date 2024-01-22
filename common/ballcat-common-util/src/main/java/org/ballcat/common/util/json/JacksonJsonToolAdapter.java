/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.common.util.json;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * @author lingting 2021/2/25 21:04
 */
public class JacksonJsonToolAdapter implements JsonTool {

	@Getter
	@Setter
	static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public void config(Consumer<ObjectMapper> consumer) {
		consumer.accept(mapper);
	}

	@SneakyThrows(JsonProcessingException.class)
	@Override
	public String toJson(Object obj) {
		return mapper.writeValueAsString(obj);
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, Class<T> r) {
		return mapper.readValue(json, r);
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
	@Override
	public <T> T toObj(String json, Type t) {
		return mapper.readValue(json, mapper.constructType(t));
	}

	@SneakyThrows({ JsonMappingException.class, JsonProcessingException.class })
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
