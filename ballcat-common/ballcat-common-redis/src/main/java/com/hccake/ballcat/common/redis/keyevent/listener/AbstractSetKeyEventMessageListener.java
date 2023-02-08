package com.hccake.ballcat.common.redis.keyevent.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

/**
 * key set event
 *
 * @author lishangbu 2023/1/12
 */
public abstract class AbstractSetKeyEventMessageListener extends AbstractKeySpaceEventMessageListener {

	private static final Topic KEYEVENT_SET_TOPIC = new PatternTopic("__keyevent@*__:set");

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	protected AbstractSetKeyEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	@Override
	public Topic getKeyEventTopic() {
		return KEYEVENT_SET_TOPIC;
	}

}
