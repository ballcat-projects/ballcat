package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.admin.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.admin.websocket.distribute.RedisMessageDistributor;
import com.hccake.ballcat.admin.websocket.distribute.RedisWebsocketMessageListener;
import com.hccake.ballcat.admin.websocket.user.UserAttributeHandshakeInterceptor;
import com.hccake.ballcat.admin.websocket.user.UserSessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class AdminWebSocketConfiguration {

	@Bean
	@ConditionalOnMissingBean(UserAttributeHandshakeInterceptor.class)
	public HandshakeInterceptor authenticationHandshakeInterceptor() {
		return new UserAttributeHandshakeInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(UserSessionKeyGenerator.class)
	public UserSessionKeyGenerator userSessionKeyGenerator() {
		return new UserSessionKeyGenerator();
	}

	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public RedisMessageDistributor messageDistributor(StringRedisTemplate stringRedisTemplate) {
		return new RedisMessageDistributor(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	public RedisWebsocketMessageListener redisWebsocketMessageDelegate(StringRedisTemplate stringRedisTemplate) {
		return new RedisWebsocketMessageListener(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean(RedisMessageListenerContainer.class)
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
			RedisWebsocketMessageListener redisWebsocketMessageListener) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(redisWebsocketMessageListener,
				new PatternTopic(RedisWebsocketMessageListener.CHANNEL));
		return container;
	}

}
