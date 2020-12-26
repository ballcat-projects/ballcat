package com.hccake.ballcat.common.websocket.config;

import com.hccake.ballcat.common.websocket.handler.ChannelHandler;
import com.hccake.ballcat.common.websocket.handler.DataHandler;
import com.hccake.ballcat.common.websocket.handler.MessageHandler;
import com.hccake.ballcat.common.websocket.handler.defaults.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

/**
 * websocket自动配置
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketAutoConfiguration {

	/**
	 * socket props
	 */
	private final WebSocketProperties webSocketProperties;

	@Bean
	@ConditionalOnMissingBean
	public WebSocketConfigurer WebSocketConfig(List<HandshakeInterceptor> handshakeInterceptor,
			WebSocketHandler webSocketHandler) {
		return registry -> registry.addHandler(webSocketHandler, webSocketProperties.getPath())
				.setAllowedOrigins(webSocketProperties.getAllowOrigins())
				.addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]));
	}

	/**
	 * 默认的websocket处理器
	 * @param channelHandler
	 * @param dataHandler
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(WebSocketHandler.class)
	public WebSocketHandler defaultWebSocketHandler(ChannelHandler channelHandler, DataHandler dataHandler) {
		return new DefaultWebSocketHandler(channelHandler, dataHandler, webSocketProperties);
	}

	/**
	 * 消息数据处理
	 * @return 数据处理
	 */
	@Bean
	@ConditionalOnMissingBean(DataHandler.class)
	public DataHandler dataHandler() {
		return new DefaultDataHandler();
	}

	/**
	 * 本地缓存
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ChannelHandler.class)
	@ConditionalOnProperty(prefix = "ws", name = "type", havingValue = "local", matchIfMissing = true)
	public ChannelHandler defaultLocalChannelHandler() {
		return new DefaultLocalChannelHandler();
	}

	/**
	 * 第三方缓存
	 * @param redisTemplate
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ChannelHandler.class)
	@ConditionalOnProperty(prefix = "ws", name = "type", havingValue = "redis")
	public ChannelHandler defaultCacheChannelHandler(RedisTemplate redisTemplate) {
		return new DefaultCacheChannelHandler(webSocketProperties, redisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean
	public MessageHandler messageHandler(ChannelHandler channelHandler) {
		return new DefaultMessageHandler(channelHandler);
	}

	/**
	 * 默认的握手拦截处理器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(HandshakeInterceptor.class)
	public HandshakeInterceptor defaultHandshakeInterceptor() {
		return new DefaultHandshakeInterceptor(webSocketProperties);
	}

}
