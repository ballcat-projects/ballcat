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

package org.ballcat.redis.serialize;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake 2019/9/9 11:07
 */
@RequiredArgsConstructor
public class JacksonSerializer implements CacheSerializer {

	private final ObjectMapper objectMapper;

	/**
	 * 反序列化方法
	 * @param cacheData 缓存中的数据
	 * @param type 反序列化目标类型
	 * @return 反序列化后的对象
	 * @throws IOException IO异常
	 */
	@Override
	public Object deserialize(String cacheData, Type type) throws IOException {
		return this.objectMapper.readValue(cacheData, CacheSerializer.getJavaType(type));
	}

	/**
	 * 序列化方法
	 * @param cacheData 待缓存的数据
	 * @return 序列化后的数据
	 * @throws IOException IO异常
	 */
	@Override
	public String serialize(Object cacheData) throws IOException {
		return this.objectMapper.writeValueAsString(cacheData);
	}

}
