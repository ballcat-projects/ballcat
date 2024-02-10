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

package org.ballcat.redis.listener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.ballcat.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis消息监听处理器
 *
 * @author huyuanzhi
 */
public abstract class AbstractMessageEventListener<T> implements MessageEventListener {

	@Autowired
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	protected StringRedisTemplate stringRedisTemplate;

	protected final Class<T> clz;

	@SuppressWarnings("unchecked")
	protected AbstractMessageEventListener() {
		Type superClass = getClass().getGenericSuperclass();
		ParameterizedType type = (ParameterizedType) superClass;
		this.clz = (Class<T>) type.getActualTypeArguments()[0];
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		byte[] channelBytes = message.getChannel();
		RedisSerializer<String> stringSerializer = this.stringRedisTemplate.getStringSerializer();
		String channelTopic = stringSerializer.deserialize(channelBytes);
		String topic = topic().getTopic();
		if (topic.equals(channelTopic)) {
			byte[] bodyBytes = message.getBody();
			String body = stringSerializer.deserialize(bodyBytes);
			T decodeMessage = JsonUtils.toObj(body, this.clz);
			handleMessage(decodeMessage);
		}
	}

	/**
	 * 处理消息
	 * @param decodeMessage 反系列化之后的消息
	 */
	protected abstract void handleMessage(T decodeMessage);

}
