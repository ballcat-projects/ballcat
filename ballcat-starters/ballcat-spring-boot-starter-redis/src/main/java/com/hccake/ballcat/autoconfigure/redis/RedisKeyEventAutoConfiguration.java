package com.hccake.ballcat.autoconfigure.redis;

import com.hccake.ballcat.common.redis.config.CacheProperties;
import com.hccake.ballcat.common.redis.keyevent.listener.*;
import com.hccake.ballcat.common.redis.keyevent.template.KeyDeletedEventMessageTemplate;
import com.hccake.ballcat.common.redis.keyevent.template.KeyExpiredEventMessageTemplate;
import com.hccake.ballcat.common.redis.keyevent.template.KeySetEventMessageTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

/**
 * redis key event auto configuration
 *
 * <p>
 * Tips:
 * </p>
 * <p>
 * extract message listener and message handler bean name that developers can cover with
 * same bean name if that behavior can make them happy
 * </p>
 *
 * @author lishangbu 2023/1/12
 */
@AutoConfiguration
public class RedisKeyEventAutoConfiguration {

	public static final String KEY_DELETED_EVENT_PREFIX = CacheProperties.PREFIX + ".key-deleted-event";

	public static final String KEY_SET_EVENT_PREFIX = CacheProperties.PREFIX + ".key-set-event";

	public static final String KEY_EXPIRED_EVENT_PREFIX = CacheProperties.PREFIX + ".key-expired-event";

	@ConditionalOnProperty(prefix = KEY_DELETED_EVENT_PREFIX, name = "enabled", havingValue = "true")
	public static class RedisKeyDeletedEventConfiguration {

		public static final String CONTAINER_NAME = "keyDeleteEventRedisMessageListenerContainer";

		public static final String LISTENER_NAME = "keyDeletedEventMessageListener";

		@Bean(name = CONTAINER_NAME)
		@ConditionalOnMissingBean(name = CONTAINER_NAME)
		public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean(name = LISTENER_NAME)
		@ConditionalOnMissingBean(name = LISTENER_NAME)
		public AbstractDeletedKeyEventMessageListener keyDeletedEventMessageListener(
				@Qualifier(value = CONTAINER_NAME) RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeyDeletedEventMessageTemplate>> objectProvider) {
			return new DefaultDeletedKeyEventMessageListener(listenerContainer, objectProvider);
		}

	}

	@ConditionalOnProperty(prefix = KEY_SET_EVENT_PREFIX, name = "enabled", havingValue = "true")
	public static class RedisKeySetEventConfiguration {

		public static final String CONTAINER_NAME = "keySetEventRedisMessageListenerContainer";

		public static final String LISTENER_NAME = "keySetEventMessageListener";

		@Bean(name = CONTAINER_NAME)
		@ConditionalOnMissingBean(name = CONTAINER_NAME)
		public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean(name = LISTENER_NAME)
		@ConditionalOnMissingBean(name = LISTENER_NAME)
		public AbstractSetKeyEventMessageListener keySetEventMessageListener(
				@Qualifier(value = CONTAINER_NAME) RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeySetEventMessageTemplate>> objectProvider) {
			return new DefaultSetKeyEventMessageListener(listenerContainer, objectProvider);
		}

	}

	@ConditionalOnProperty(prefix = KEY_EXPIRED_EVENT_PREFIX, name = "enabled", havingValue = "true")
	public static class RedisKeyExpiredEventConfiguration {

		public static final String CONTAINER_NAME = "keyExpiredEventRedisMessageListenerContainer";

		public static final String LISTENER_NAME = "keyExpiredEventMessageListener";

		@Bean(name = CONTAINER_NAME)
		@ConditionalOnMissingBean(name = CONTAINER_NAME)
		public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean(name = LISTENER_NAME)
		@ConditionalOnMissingBean(name = LISTENER_NAME)
		public AbstractExpiredKeyEventMessageListener keyExpiredEventMessageListener(
				@Qualifier(value = CONTAINER_NAME) RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeyExpiredEventMessageTemplate>> objectProvider) {
			return new DefaultExpiredKeyEventMessageListener(listenerContainer, objectProvider);
		}

	}

}
