package com.hccake.extend.redis.moudle;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 抽象的 Redis module 操作类 实现方式基于 org.springframework.data.redis.connection.lettuce
 *
 * @link https://stackoverflow.com/questions/61062171/spring-data-redis-support-for-modules
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

		List<byte[]> extraArgs = Arrays.stream(args).filter(StringUtils::hasLength)
				.map(arg -> valueSerializer.serialize(arg)).collect(Collectors.toList());

		CommandArgs<byte[], byte[]> commandArgs = new CommandArgs<>(codec).addKey(keySerializer.serialize(key))
				.addValues(extraArgs);

		try (LettuceConnection connection = (LettuceConnection) connectionFactory.getConnection()) {
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