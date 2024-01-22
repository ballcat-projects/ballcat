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

package org.ballcat.websocket.session;

import org.ballcat.websocket.handler.ConcurrentWebSocketSessionOptions;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocketHandler 装饰器，该装饰器主要用于在开启和关闭连接时，进行session的映射存储与释放
 *
 * @author Hccake 2020/12/31
 *
 */
public class MapSessionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final WebSocketSessionStore webSocketSessionStore;

	private final ConcurrentWebSocketSessionOptions concurrentWebSocketSessionOptions;

	public MapSessionWebSocketHandlerDecorator(WebSocketHandler delegate, WebSocketSessionStore webSocketSessionStore,
			ConcurrentWebSocketSessionOptions concurrentWebSocketSessionOptions) {
		super(delegate);
		this.webSocketSessionStore = webSocketSessionStore;
		this.concurrentWebSocketSessionOptions = concurrentWebSocketSessionOptions;
	}

	/**
	 * websocket 连接时执行的动作
	 * @param wsSession websocket session 对象
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession wsSession) {
		// 包装一层，防止并发发送出现问题
		if (Boolean.TRUE.equals(this.concurrentWebSocketSessionOptions.isEnable())) {
			wsSession = new ConcurrentWebSocketSessionDecorator(wsSession,
					this.concurrentWebSocketSessionOptions.getSendTimeLimit(),
					this.concurrentWebSocketSessionOptions.getBufferSizeLimit(),
					this.concurrentWebSocketSessionOptions.getOverflowStrategy());
		}
		this.webSocketSessionStore.addSession(wsSession);
	}

	/**
	 * websocket 关闭连接时执行的动作
	 * @param wsSession websocket session 对象
	 * @param closeStatus 关闭状态对象
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus closeStatus) throws Exception {
		this.webSocketSessionStore.removeSession(wsSession);
	}

}
