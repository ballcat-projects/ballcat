package com.hccake.ballcat.common.redis.keyevent.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

/**
 * key expired event
 * <p>
 * similar with {@link KeyExpirationEventMessageListener}
 * </p>
 *
 * @author lishangbu 2023/1/12
 */
public abstract class AbstractExpiredKeyEventMessageListener extends AbstractKeySpaceEventMessageListener {

	private static final Topic KEYEVENT_EXPIRED_TOPIC = new PatternTopic("__keyevent@*__:expired");

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	protected AbstractExpiredKeyEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	@Override
	public Topic getKeyEventTopic() {
		return KEYEVENT_EXPIRED_TOPIC;
	}

}
