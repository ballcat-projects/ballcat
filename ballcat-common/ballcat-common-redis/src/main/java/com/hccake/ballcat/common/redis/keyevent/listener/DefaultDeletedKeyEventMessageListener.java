package com.hccake.ballcat.common.redis.keyevent.listener;

import com.hccake.ballcat.common.redis.keyevent.template.KeyDeletedEventMessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * default key deleted event handler
 *
 * @author lishangbu
 * @date 2023/1/12
 */
@Slf4j
public class DefaultDeletedKeyEventMessageListener extends AbstractDeletedKeyEventMessageListener {

	protected List<KeyDeletedEventMessageTemplate> keyDeletedEventMessageTemplates;

	/**
	 * Creates new {@link MessageListener} for specific messages.
	 * @param listenerContainer must not be {@literal null}.
	 */
	public DefaultDeletedKeyEventMessageListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	public DefaultDeletedKeyEventMessageListener(RedisMessageListenerContainer listenerContainer,
			ObjectProvider<List<KeyDeletedEventMessageTemplate>> objectProvider) {
		super(listenerContainer);
		objectProvider.ifAvailable(templates -> this.keyDeletedEventMessageTemplates = new ArrayList<>(templates));
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		if (CollectionUtils.isEmpty(keyDeletedEventMessageTemplates)) {
			return;
		}
		super.onMessage(message, pattern);
		String setKey = message.toString();
		// 监听key信息新增/修改事件
		for (KeyDeletedEventMessageTemplate keyDeletedEventMessageTemplate : keyDeletedEventMessageTemplates) {
			if (keyDeletedEventMessageTemplate.support(setKey)) {
				if (log.isTraceEnabled()) {
					log.trace("use template [{}] handle key deleted event,the deleted key is [{}]",
							keyDeletedEventMessageTemplate.getClass().getName(), setKey);
				}
				keyDeletedEventMessageTemplate.handleMessage(setKey);
			}
		}

	}

}
