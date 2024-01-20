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
 * 普通文本类型（非指定json类型）的消息处理器 即消息不满足于我们定义的Json类型消息时，所使用的处理器
 *
 * @see JsonWebSocketMessage
 * @author Hccake 2021/1/5
 *
 */
public interface PlanTextMessageHandler {

	/**
	 * 普通文本消息处理
	 * @param session 当前接收消息的session
	 * @param message 文本消息
	 */
	void handle(WebSocketSession session, String message);

}
