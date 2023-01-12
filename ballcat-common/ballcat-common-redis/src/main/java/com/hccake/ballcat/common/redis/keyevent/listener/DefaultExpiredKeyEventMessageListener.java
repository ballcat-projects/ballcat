package com.hccake.ballcat.common.redis.keyevent.listener;

import com.hccake.ballcat.common.redis.keyevent.template.KeyExpiredEventMessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * default key expired event handler
 *
 * @author lishangbu
 * @date 2023/1/12
 */
@Slf4j
public class DefaultExpiredKeyEventMessageListener extends AbstractExpiredKeyEventMessageListener {

	@SuppressWarnings("all")
	@Autowired(required = false)
	@Nullable
	protected List<KeyExpiredEventMessageTemplate> keyExpiredEventMessageTemplates;

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public DefaultExpiredKeyEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		if (CollectionUtils.isEmpty(keyExpiredEventMessageTemplates)) {
			return;
		}
		super.onMessage(message, pattern);
		String expiredKey = message.toString();
		// listening key expired event
		for (KeyExpiredEventMessageTemplate keyExpiredEventMessageTemplate : keyExpiredEventMessageTemplates) {
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
