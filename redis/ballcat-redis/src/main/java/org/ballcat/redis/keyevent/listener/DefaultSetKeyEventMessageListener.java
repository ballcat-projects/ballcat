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

package org.ballcat.redis.keyevent.listener;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.redis.keyevent.template.KeySetEventMessageTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

/**
 * default key set event handler
 *
 * @author lishangbu 2023/1/12
 */
@Slf4j
public class DefaultSetKeyEventMessageListener extends AbstractSetKeyEventMessageListener {

	protected List<KeySetEventMessageTemplate> keySetEventMessageTemplates;

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public DefaultSetKeyEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	public DefaultSetKeyEventMessageListener(RedisMessageListenerContainer listenerContainer,
			ObjectProvider<List<KeySetEventMessageTemplate>> objectProvider) {
		super(listenerContainer);
		objectProvider.ifAvailable(templates -> this.keySetEventMessageTemplates = new ArrayList<>(templates));
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		if (CollectionUtils.isEmpty(this.keySetEventMessageTemplates)) {
			return;
		}
		super.onMessage(message, pattern);
		String setKey = message.toString();
		// 监听key信息新增/修改事件
		for (KeySetEventMessageTemplate keySetEventMessageTemplate : this.keySetEventMessageTemplates) {
			if (keySetEventMessageTemplate.support(setKey)) {
				if (log.isTraceEnabled()) {
					log.trace("use template[{}]handle key set event,the set key is[{}]",
							keySetEventMessageTemplate.getClass().getName(), setKey);
				}
				keySetEventMessageTemplate.handleMessage(setKey);
			}
		}

	}

}
