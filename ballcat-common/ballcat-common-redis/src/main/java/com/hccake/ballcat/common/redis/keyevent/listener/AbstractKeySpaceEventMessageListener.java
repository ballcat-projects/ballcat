package com.hccake.ballcat.common.redis.keyevent.listener;

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
		if (publisher != null) {
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
