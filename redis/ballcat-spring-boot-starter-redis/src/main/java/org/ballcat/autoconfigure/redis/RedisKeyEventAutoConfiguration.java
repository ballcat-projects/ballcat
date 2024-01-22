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

package org.ballcat.autoconfigure.redis;

import java.util.List;

import org.ballcat.redis.config.CacheProperties;
import org.ballcat.redis.keyevent.listener.AbstractDeletedKeyEventMessageListener;
import org.ballcat.redis.keyevent.listener.AbstractExpiredKeyEventMessageListener;
import org.ballcat.redis.keyevent.listener.AbstractSetKeyEventMessageListener;
import org.ballcat.redis.keyevent.listener.DefaultDeletedKeyEventMessageListener;
import org.ballcat.redis.keyevent.listener.DefaultExpiredKeyEventMessageListener;
import org.ballcat.redis.keyevent.listener.DefaultSetKeyEventMessageListener;
import org.ballcat.redis.keyevent.template.KeyDeletedEventMessageTemplate;
import org.ballcat.redis.keyevent.template.KeyExpiredEventMessageTemplate;
import org.ballcat.redis.keyevent.template.KeySetEventMessageTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

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
				@Qualifier(CONTAINER_NAME) RedisMessageListenerContainer listenerContainer,
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
				@Qualifier(CONTAINER_NAME) RedisMessageListenerContainer listenerContainer,
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
				@Qualifier(CONTAINER_NAME) RedisMessageListenerContainer listenerContainer,
				ObjectProvider<List<KeyExpiredEventMessageTemplate>> objectProvider) {
			return new DefaultExpiredKeyEventMessageListener(listenerContainer, objectProvider);
		}

	}

}
