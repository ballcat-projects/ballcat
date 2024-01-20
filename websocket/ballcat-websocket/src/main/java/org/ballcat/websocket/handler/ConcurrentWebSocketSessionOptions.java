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

import lombok.Data;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

/**
 * 并发使用 WebSocketSession 的相关配置
 *
 * @author hccake
 */
@Data
public class ConcurrentWebSocketSessionOptions {

	/**
	 * 是否在多线程环境下进行发送
	 */
	private boolean enable = false;

	/**
	 * 发送时间的限制（ms）
	 */
	private int sendTimeLimit = 1000 * 5;

	/**
	 * 发送消息缓冲上限 (byte)
	 */
	private int bufferSizeLimit = 1024 * 100;

	/**
	 * 溢出时的执行策略
	 */
	ConcurrentWebSocketSessionDecorator.OverflowStrategy overflowStrategy = ConcurrentWebSocketSessionDecorator.OverflowStrategy.TERMINATE;

}
