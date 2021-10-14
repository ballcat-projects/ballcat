package com.hccake.ballcat.common.websocket.distribute;

import cn.hutool.core.collection.CollectionUtil;
import com.hccake.ballcat.common.websocket.WebSocketMessageSender;

import java.util.List;

/**
 * 消息分发器
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public interface MessageDistributor {

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	void distribute(MessageDO messageDO);

	/**
	 * 发送消息
	 * @param messageDO 发送的消息
	 */
	default void doSend(MessageDO messageDO) {
		Boolean needBroadcast = messageDO.getNeedBroadcast();
		String messageText = messageDO.getMessageText();
		List<Object> sessionKeys = messageDO.getSessionKeys();
		if (needBroadcast != null && needBroadcast) {
			// 广播信息
			WebSocketMessageSender.broadcast(messageText);
		}
		else if (CollectionUtil.isNotEmpty(sessionKeys)) {
			// 指定用户发送
			for (Object sessionKey : sessionKeys) {
				WebSocketMessageSender.send(sessionKey, messageText);
			}
		}
	}

}
