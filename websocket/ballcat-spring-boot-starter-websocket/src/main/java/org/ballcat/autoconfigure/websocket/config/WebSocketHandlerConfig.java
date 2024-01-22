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

package org.ballcat.autoconfigure.websocket.config;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.websocket.WebSocketProperties;
import org.ballcat.websocket.handler.CustomWebSocketHandler;
import org.ballcat.websocket.handler.PlanTextMessageHandler;
import org.ballcat.websocket.session.DefaultWebSocketSessionStore;
import org.ballcat.websocket.session.MapSessionWebSocketHandlerDecorator;
import org.ballcat.websocket.session.SessionKeyGenerator;
import org.ballcat.websocket.session.WebSocketSessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;

/**
 * @author Hccake 2021/1/5
 *
 */
@RequiredArgsConstructor
public class WebSocketHandlerConfig {

	private final WebSocketProperties webSocketProperties;

	/**
	 * WebSocket session 存储器
	 * @return DefaultWebSocketSessionStore
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebSocketSessionStore webSocketSessionStore(
			@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator) {
		return new DefaultWebSocketSessionStore(sessionKeyGenerator);
	}

	@Bean
	@ConditionalOnMissingBean(WebSocketHandler.class)
	public WebSocketHandler webSocketHandler(WebSocketSessionStore webSocketSessionStore,
			@Autowired(required = false) PlanTextMessageHandler planTextMessageHandler) {
		CustomWebSocketHandler customWebSocketHandler = new CustomWebSocketHandler(planTextMessageHandler);
		if (this.webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, webSocketSessionStore,
					this.webSocketProperties.getConcurrent());
		}
		return customWebSocketHandler;
	}

}
