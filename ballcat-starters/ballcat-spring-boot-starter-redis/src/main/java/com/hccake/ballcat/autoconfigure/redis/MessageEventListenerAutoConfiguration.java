package com.hccake.ballcat.autoconfigure.redis;

import com.hccake.ballcat.common.redis.listener.MessageEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author hccake
 */
@Import(AddMessageEventListenerToContainer.class)
@ConditionalOnBean(MessageEventListener.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class MessageEventListenerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(RedisMessageListenerContainer.class)
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

}
