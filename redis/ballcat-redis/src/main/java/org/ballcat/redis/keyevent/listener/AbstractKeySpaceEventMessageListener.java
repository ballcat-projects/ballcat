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

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisKeyspaceEvent;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * {@link MessageListener} publishing {@link RedisKeyspaceEvent}s via
 * {@link ApplicationEventPublisher} by listening to Redis keyspace notifications for
 * specific key event.
 *
 * @author lishangbu 2023/1/12
 */
public abstract class AbstractKeySpaceEventMessageListener extends KeyspaceEventMessageListener
		implements ApplicationEventPublisherAware {

	@Nullable
	protected ApplicationEventPublisher publisher;

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	protected AbstractKeySpaceEventMessageListener(@NonNull RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	/**
	 * Register instance within the container.
	 * @param listenerContainer never {@literal null}.
	 */
	@Override
	protected void doRegister(@NonNull RedisMessageListenerContainer listenerContainer) {
		listenerContainer.addMessageListener(this, getKeyEventTopic());
	}

	/**
	 * Handle the actual message
	 * @param message never {@literal null}.
	 */
	@Override
	protected void doHandleMessage(@NonNull Message message) {
		publishEvent(new RedisKeyExpiredEvent<>(message.getBody()));
	}

	/**
	 * Publish the event in case an {@link ApplicationEventPublisher} is set.
	 * @param event can be {@literal null}.
	 */
	protected void publishEvent(RedisKeyExpiredEvent<?> event) {
		if (this.publisher != null) {
			this.publisher.publishEvent(event);
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	/**
	 * Creates new {@link Topic} for listening
	 * @return Topic
	 */
	public abstract Topic getKeyEventTopic();

}
