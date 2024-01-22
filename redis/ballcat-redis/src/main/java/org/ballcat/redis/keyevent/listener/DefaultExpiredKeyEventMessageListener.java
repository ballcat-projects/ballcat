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
import org.ballcat.redis.keyevent.template.KeyExpiredEventMessageTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

/**
 * default key expired event handler
 *
 * @author lishangbu 2023/1/12
 */
@Slf4j
public class DefaultExpiredKeyEventMessageListener extends AbstractExpiredKeyEventMessageListener {

	protected List<KeyExpiredEventMessageTemplate> keyExpiredEventMessageTemplates;

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public DefaultExpiredKeyEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	public DefaultExpiredKeyEventMessageListener(RedisMessageListenerContainer listenerContainer,
			ObjectProvider<List<KeyExpiredEventMessageTemplate>> objectProvider) {
		super(listenerContainer);
		objectProvider.ifAvailable(templates -> this.keyExpiredEventMessageTemplates = new ArrayList<>(templates));
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		if (CollectionUtils.isEmpty(this.keyExpiredEventMessageTemplates)) {
			return;
		}
		super.onMessage(message, pattern);
		String expiredKey = message.toString();
		// listening key expired event
		for (KeyExpiredEventMessageTemplate keyExpiredEventMessageTemplate : this.keyExpiredEventMessageTemplates) {
			if (keyExpiredEventMessageTemplate.support(expiredKey)) {
				if (log.isTraceEnabled()) {
					log.trace("use template[{}]handle key expired event,the expired key is [{}]",
							keyExpiredEventMessageTemplate.getClass().getName(), expiredKey);
				}
				keyExpiredEventMessageTemplate.handleMessage(expiredKey);
			}
		}

	}

}
