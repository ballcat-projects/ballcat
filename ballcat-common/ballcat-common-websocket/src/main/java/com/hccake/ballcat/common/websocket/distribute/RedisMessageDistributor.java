package com.hccake.ballcat.common.websocket.distribute;

import com.hccake.ballcat.common.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 基于 redis PUB/SUB 的消息分发器, 订阅 websocket 发送消息，接收到消息时进行推送
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@RequiredArgsConstructor
public class RedisMessageDistributor implements MessageDistributor, MessageListener {

	public static final String CHANNEL = "websocket-send";

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		String str = JsonUtils.toJson(messageDO);
		stringRedisTemplate.convertAndSend(CHANNEL, str);
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		byte[] channelBytes = message.getChannel();
		RedisSerializer<String> stringSerializer = stringRedisTemplate.getStringSerializer();
		String channel = stringSerializer.deserialize(channelBytes);

		// 这里没有使用通配符，所以一定是true
		if (CHANNEL.equals(channel)) {
			byte[] bodyBytes = message.getBody();
			String body = stringSerializer.deserialize(bodyBytes);
			MessageDO messageDO = JsonUtils.toObj(body, MessageDO.class);
			doSend(messageDO);
		}
	}

}
