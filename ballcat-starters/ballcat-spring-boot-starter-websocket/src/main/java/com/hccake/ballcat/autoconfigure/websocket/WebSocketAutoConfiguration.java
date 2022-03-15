package com.hccake.ballcat.autoconfigure.websocket;

import com.hccake.ballcat.common.websocket.handler.JsonMessageHandler;
import com.hccake.ballcat.common.websocket.holder.JsonMessageHandlerHolder;
import com.hccake.ballcat.common.websocket.message.JsonWebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * websocket自动配置
 *
 * @author Yakir
 */
@Import(WebSocketHandlerConfig.class)
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketAutoConfiguration {

	private final WebSocketProperties webSocketProperties;

	private final List<JsonMessageHandler<JsonWebSocketMessage>> jsonMessageHandlerList;

	@Bean
	@ConditionalOnMissingBean
	public WebSocketConfigurer webSocketConfigurer(List<HandshakeInterceptor> handshakeInterceptor,
			WebSocketHandler webSocketHandler) {
		return registry -> {
			WebSocketHandlerRegistration registration = registry
					.addHandler(webSocketHandler, webSocketProperties.getPath())
					.addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]));

			String[] allowedOrigins = webSocketProperties.getAllowedOrigins();
			if (allowedOrigins != null && allowedOrigins.length > 0) {
				registration.setAllowedOrigins(allowedOrigins);
			}

			String[] allowedOriginPatterns = webSocketProperties.getAllowedOriginPatterns();
			if (allowedOriginPatterns != null && allowedOriginPatterns.length > 0) {
				registration.setAllowedOriginPatterns(allowedOriginPatterns);
			}
		};
	}

	/**
	 * 初始化时将所有的jsonMessageHandler注册到JsonMessageHandlerHolder中
	 */
	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler<JsonWebSocketMessage> jsonMessageHandler : jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler(jsonMessageHandler);
		}
	}

}
