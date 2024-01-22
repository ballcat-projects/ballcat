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

package org.ballcat.websocket.distribute;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.websocket.WebSocketMessageSender;
import org.ballcat.websocket.session.WebSocketSessionStore;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author hccake
 */
@Slf4j
public abstract class AbstractMessageDistributor implements MessageDistributor {

	private final WebSocketSessionStore webSocketSessionStore;

	protected AbstractMessageDistributor(WebSocketSessionStore webSocketSessionStore) {
		this.webSocketSessionStore = webSocketSessionStore;
	}

	/**
	 * 对当前服务中的 websocket 连接做消息推送
	 * @param messageDO 消息实体
	 */
	protected void doSend(MessageDO messageDO) {

		// 是否广播发送
		Boolean needBroadcast = messageDO.getNeedBroadcast();

		// 获取待发送的 sessionKeys
		Collection<Object> sessionKeys;
		if (needBroadcast != null && needBroadcast) {
			sessionKeys = this.webSocketSessionStore.getSessionKeys();
		}
		else {
			sessionKeys = messageDO.getSessionKeys();
		}
		if (CollectionUtils.isEmpty(sessionKeys)) {
			log.warn("发送 websocket 消息，却没有找到对应 sessionKeys, messageDo: {}", messageDO);
			return;
		}

		String messageText = messageDO.getMessageText();
		Boolean onlyOneClientInSameKey = messageDO.getOnlyOneClientInSameKey();

		for (Object sessionKey : sessionKeys) {
			Collection<WebSocketSession> sessions = this.webSocketSessionStore.getSessions(sessionKey);
			if (!CollectionUtils.isEmpty(sessions)) {
				// 相同 sessionKey 的客户端只推送一次操作
				if (onlyOneClientInSameKey != null && onlyOneClientInSameKey) {
					WebSocketSession wsSession = sessions.iterator().next();
					WebSocketMessageSender.send(wsSession, messageText);
					continue;
				}
				for (WebSocketSession wsSession : sessions) {
					WebSocketMessageSender.send(wsSession, messageText);
				}
			}
		}
	}

}
