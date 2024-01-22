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

package org.ballcat.redis.moudle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.ProtocolKeyword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

/**
 * 抽象的 Redis module 操作类 实现方式基于 org.springframework.data.redis.connection.lettuce
 *
 * @link <a href=
 * "https://stackoverflow.com/questions/61062171/spring-data-redis-support-for-modules">spring-data-redis-support-for-modules</a>
 */
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractRedisModuleHelper {

	private final LettuceConnectionFactory connectionFactory;

	/**
	 * key 序列化器
	 */
	@SuppressWarnings("rawtypes")
	private RedisSerializer keySerializer = RedisSerializer.string();

	/**
	 * value 序列化器
	 */
	@SuppressWarnings("rawtypes")
	private RedisSerializer valueSerializer = RedisSerializer.string();

	protected ByteArrayCodec codec = ByteArrayCodec.INSTANCE;

	@SuppressWarnings("unchecked")
	protected <T> Optional<T> execute(String key, ProtocolKeyword type, CommandOutput<byte[], byte[], T> output,
			String... args) {

		List<byte[]> extraArgs = Arrays.stream(args)
			.filter(StringUtils::hasLength)
			.map(arg -> this.valueSerializer.serialize(arg))
			.collect(Collectors.toList());

		CommandArgs<byte[], byte[]> commandArgs = new CommandArgs<>(this.codec)
			.addKey(this.keySerializer.serialize(key))
			.addValues(extraArgs);

		try (LettuceConnection connection = (LettuceConnection) this.connectionFactory.getConnection()) {
			RedisFuture<T> future = connection.getNativeConnection().dispatch(type, output, commandArgs);
			return Optional.ofNullable(future.get());
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		catch (Exception e) {
			log.error("[execute] 执行异常, KEY: [{}], type: [{}], args: [{}]", key, type.name(), args, e);
		}

		return Optional.empty();

	}

}
