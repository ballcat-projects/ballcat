package com.hccake.ballcat.autoconfigure.redis;

import com.hccake.ballcat.common.redis.listener.MessageEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author hccake
 */
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

	@Configuration(proxyBeanMethods = false)
	@RequiredArgsConstructor
	static class AddMessageEventListenerToContainer {

		private final RedisMessageListenerContainer listenerContainer;

		private final List<MessageEventListener> listenerList;

		/**
		 * 将所有的 <code>MessageEventListener<code/> 注册到
		 * <code>RedisMessageListenerContainer<code/> 中
		 */
		@PostConstruct
		public void addMessageListener() {
			// 注册监听器
			for (MessageEventListener messageEventListener : listenerList) {
				listenerContainer.addMessageListener(messageEventListener, messageEventListener.topic());
			}
		}

	}

}
