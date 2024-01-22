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

package org.ballcat.websocket.distribute;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.websocket.session.WebSocketSessionStore;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 基于 redis PUB/SUB 的消息分发器, 订阅 websocket 发送消息，接收到消息时进行推送
 *
 * @author Hccake 2021/1/12
 *
 */
@Slf4j
public class RedisMessageDistributor extends AbstractMessageDistributor implements MessageListener {

	public static final String CHANNEL = "websocket-send";

	private final StringRedisTemplate stringRedisTemplate;

	public RedisMessageDistributor(WebSocketSessionStore webSocketSessionStore,
			StringRedisTemplate stringRedisTemplate) {
		super(webSocketSessionStore);
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		String str = JsonUtils.toJson(messageDO);
		this.stringRedisTemplate.convertAndSend(CHANNEL, str);
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		log.info("redis channel Listener message send {}", message);
		byte[] channelBytes = message.getChannel();
		RedisSerializer<String> stringSerializer = this.stringRedisTemplate.getStringSerializer();
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
