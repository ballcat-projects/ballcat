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

package org.ballcat.websocket.handler;

import org.ballcat.websocket.message.JsonWebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Hccake 2021/1/4
 *
 */
public interface JsonMessageHandler<T extends JsonWebSocketMessage> {

	/**
	 * JsonWebSocketMessage 类型消息处理
	 * @param session 当前接收 session
	 * @param message 当前接收到的 message
	 */
	void handle(WebSocketSession session, T message);

	/**
	 * 当前处理器处理的消息类型
	 * @return messageType
	 */
	String type();

	/**
	 * 当前处理器对应的消息Class
	 * @return Class<T>
	 */
	Class<T> getMessageClass();

}
