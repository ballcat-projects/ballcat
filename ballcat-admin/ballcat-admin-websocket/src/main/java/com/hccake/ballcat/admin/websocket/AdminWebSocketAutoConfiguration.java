package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.admin.websocket.user.UserAttributeHandshakeInterceptor;
import com.hccake.ballcat.admin.websocket.user.UserSessionKeyGenerator;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.common.websocket.distribute.RedisMessageDistributor;
import com.hccake.ballcat.common.websocket.distribute.RedisWebsocketMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Import({ SystemWebsocketEventListenerConfiguration.class, NotifyWebsocketEventListenerConfiguration.class })
@Configuration
@RequiredArgsConstructor
public class AdminWebSocketAutoConfiguration {

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

}
