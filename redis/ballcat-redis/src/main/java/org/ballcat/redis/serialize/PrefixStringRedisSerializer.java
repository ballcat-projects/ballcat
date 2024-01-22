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

import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.redis.prefix.IRedisPrefixConverter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 自定义 String Key 序列化工具，添加全局key前缀
 *
 * @author Hccake 2020/3/27 22:57
 */
@Slf4j
public class PrefixStringRedisSerializer extends StringRedisSerializer {

	private final IRedisPrefixConverter iRedisPrefixConverter;

	public PrefixStringRedisSerializer(IRedisPrefixConverter iRedisPrefixConverter) {
		super(StandardCharsets.UTF_8);
		this.iRedisPrefixConverter = iRedisPrefixConverter;
	}

	@Override
	public String deserialize(byte[] bytes) {
		byte[] unwrap = this.iRedisPrefixConverter.unwrap(bytes);
		return super.deserialize(unwrap);
	}

	@Override
	public byte[] serialize(String key) {
		byte[] originBytes = super.serialize(key);
		return this.iRedisPrefixConverter.wrap(originBytes);
	}

}
