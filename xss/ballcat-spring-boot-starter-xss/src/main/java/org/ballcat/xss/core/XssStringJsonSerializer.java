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

package org.ballcat.xss.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ballcat.xss.cleaner.XssCleaner;

/**
 * XSS过滤 jackson 序列化器
 *
 * @author Hccake 2019/10/17 22:23
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

	private final XssCleaner xssCleaner;

	public XssStringJsonSerializer(XssCleaner xssCleaner) {
		this.xssCleaner = xssCleaner;
	}

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (value != null) {
			// 开启 Xss 才进行处理
			if (XssStateHolder.enabled()) {
				value = this.xssCleaner.clean(value);
			}
			jsonGenerator.writeString(value);
		}
	}

}
