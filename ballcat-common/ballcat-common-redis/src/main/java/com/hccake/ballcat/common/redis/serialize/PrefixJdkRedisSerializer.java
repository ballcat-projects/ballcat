package com.hccake.ballcat.common.redis.serialize;

import com.hccake.ballcat.common.redis.prefix.IRedisPrefixConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 22:57 自定义Key序列化工具，添加全局key前缀
 */
@Slf4j
public class PrefixJdkRedisSerializer extends JdkSerializationRedisSerializer {

	private final IRedisPrefixConverter redisPrefixConverter;

	public PrefixJdkRedisSerializer(IRedisPrefixConverter redisPrefixConverter) {
		this.redisPrefixConverter = redisPrefixConverter;
	}

	@Override
	public Object deserialize(byte[] bytes) {
		byte[] unwrap = redisPrefixConverter.unwrap(bytes);
		return super.deserialize(unwrap);
	}

	@Override
	public byte[] serialize(Object object) {
		byte[] originBytes = super.serialize(object);
		return redisPrefixConverter.wrap(originBytes);
	}

}
