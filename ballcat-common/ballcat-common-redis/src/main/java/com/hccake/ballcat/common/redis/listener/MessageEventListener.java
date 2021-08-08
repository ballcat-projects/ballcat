package com.hccake.ballcat.common.redis.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

/**
 * PUB/SUB 模式中的消息监听者
 *
 * @author hccake
 */
public interface MessageEventListener extends MessageListener {

	/**
	 * 订阅者订阅的话题
	 * @return topic
	 */
	Topic topic();

}
