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

package org.ballcat.desensitize.json;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ballcat.desensitize.annotation.AnnotationDesensitizeFunction;
import org.ballcat.desensitize.annotation.AnnotationHandlerHolder;

/**
 * Jackson脱敏处理序列化器
 *
 * @author Hccake 2021/1/22
 *
 */
public class JsonDesensitizeSerializer extends JsonSerializer<Object> {

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
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers)
			throws IOException {
		if (value instanceof String) {
			String str = (String) value;

			String fieldName = jsonGenerator.getOutputContext().getCurrentName();
			// 未开启脱敏
			if (this.desensitizeStrategy != null && this.desensitizeStrategy.ignoreField(fieldName)) {
				jsonGenerator.writeString(str);
				return;
			}
			AnnotationDesensitizeFunction handleFunction = AnnotationHandlerHolder
				.getHandleFunction(this.jsonDesensitizeAnnotation.annotationType());
			if (handleFunction == null) {
				jsonGenerator.writeString(str);
				return;
			}
			jsonGenerator.writeString(handleFunction.mask(this.jsonDesensitizeAnnotation, str));
		}
	}

}
