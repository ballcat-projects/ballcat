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

package org.ballcat.autoconfigure.websocket;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.websocket.config.LocalMessageDistributorConfig;
import org.ballcat.autoconfigure.websocket.config.RedisMessageDistributorConfig;
import org.ballcat.autoconfigure.websocket.config.RocketMqMessageDistributorConfig;
import org.ballcat.autoconfigure.websocket.config.WebSocketHandlerConfig;
import org.ballcat.websocket.handler.JsonMessageHandler;
import org.ballcat.websocket.handler.PingJsonMessageHandler;
import org.ballcat.websocket.holder.JsonMessageHandlerInitializer;
import org.ballcat.websocket.message.JsonWebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * websocket自动配置
 *
 * @author Yakir Hccake
 */
@AutoConfiguration
@Import({ WebSocketHandlerConfig.class, LocalMessageDistributorConfig.class, RedisMessageDistributorConfig.class,
		RocketMqMessageDistributorConfig.class })
@EnableWebSocket
@RequiredArgsConstructor
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketAutoConfiguration {

	private final WebSocketProperties webSocketProperties;

	@Bean
	@ConditionalOnMissingBean
	public WebSocketConfigurer webSocketConfigurer(List<HandshakeInterceptor> handshakeInterceptor,
			WebSocketHandler webSocketHandler,
			@Autowired(required = false) SockJsServiceConfigurer sockJsServiceConfigurer) {
		return registry -> {
			WebSocketHandlerRegistration registration = registry
				.addHandler(webSocketHandler, this.webSocketProperties.getPath())
				.addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]));

			String[] allowedOrigins = this.webSocketProperties.getAllowedOrigins();
			if (allowedOrigins != null && allowedOrigins.length > 0) {
				registration.setAllowedOrigins(allowedOrigins);
			}

			String[] allowedOriginPatterns = this.webSocketProperties.getAllowedOriginPatterns();
			if (allowedOriginPatterns != null && allowedOriginPatterns.length > 0) {
				registration.setAllowedOriginPatterns(allowedOriginPatterns);
			}

			if (this.webSocketProperties.isWithSockjs()) {
				SockJsServiceRegistration sockJsServiceRegistration = registration.withSockJS();
				if (sockJsServiceConfigurer != null) {
					sockJsServiceConfigurer.config(sockJsServiceRegistration);
				}
			}
		};
	}

	/**
	 * 心跳处理器
	 * @return PingJsonMessageHandler
	 */
	@Bean
	@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "heartbeat", havingValue = "true",
			matchIfMissing = true)
	public PingJsonMessageHandler pingJsonMessageHandler() {
		return new PingJsonMessageHandler();
	}

	/**
	 * 注册 JsonMessageHandlerInitializer 收集所有的 json 类型消息处理器
	 * @param jsonMessageHandlerList json 类型消息处理器
	 * @return JsonMessageHandlerInitializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public JsonMessageHandlerInitializer jsonMessageHandlerInitializer(
			List<JsonMessageHandler<? extends JsonWebSocketMessage>> jsonMessageHandlerList) {
		return new JsonMessageHandlerInitializer(jsonMessageHandlerList);
	}

}
