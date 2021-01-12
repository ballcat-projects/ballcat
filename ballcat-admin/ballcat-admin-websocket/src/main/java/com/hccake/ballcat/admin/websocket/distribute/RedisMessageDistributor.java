package com.hccake.ballcat.admin.websocket.distribute;

import com.hccake.ballcat.common.core.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 消息分发器
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@RequiredArgsConstructor
public class RedisMessageDistributor implements MessageDistributor {

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		String str = JacksonUtils.toJson(messageDO);
		stringRedisTemplate.convertAndSend(RedisWebsocketMessageListener.CHANNEL, str);
	}

}
