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

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.ballcat.websocket.distribute.LocalMessageDistributor;
import org.ballcat.websocket.distribute.MessageDistributor;
import org.ballcat.websocket.session.MapSessionWebSocketHandlerDecorator;
import org.ballcat.websocket.session.SessionKeyGenerator;
import org.ballcat.websocket.session.WebSocketSessionStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static org.assertj.core.api.Assertions.assertThat;

public class WebSocketAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(WebSocketAutoConfiguration.class));

	@Test
	void requiresSessionKeyGeneratorWhenMapSessionEnabled() {
		this.contextRunner.run(ctx -> {
			assertThat(ctx).hasFailed();
			assertThat(ctx.getStartupFailure()).hasMessageContaining("SessionKeyGenerator");
		});
	}

	@Test
	void doesNotRequireSessionKeyGeneratorWhenMapSessionDisabled() {
		this.contextRunner.withPropertyValues("ballcat.websocket.map-session=false").run(ctx -> {
			assertThat(ctx).hasNotFailed();
			assertThat(ctx).hasBean("webSocketHandler");
			assertThat(ctx).doesNotHaveBean(WebSocketSessionStore.class);
			assertThat(ctx).doesNotHaveBean(MessageDistributor.class);
		});
	}

	@Test
	void createsSessionStoreAndLocalDistributorWhenMapSessionEnabled() {
		this.contextRunner.withBean(SessionKeyGenerator.class, () -> WebSocketSession::getId).run(ctx -> {
			assertThat(ctx).hasSingleBean(WebSocketSessionStore.class);
			assertThat(ctx).hasSingleBean(LocalMessageDistributor.class);
			assertThat(ctx).getBean("webSocketHandler").isInstanceOf(MapSessionWebSocketHandlerDecorator.class);
		});
	}

	@Test
	void backsOffDefaultHandlerWhenUserWebSocketHandlerExists() {
		this.contextRunner.withBean(SessionKeyGenerator.class, () -> WebSocketSession::getId)
			.withUserConfiguration(OtherWebSocketHandlerConfiguration.class)
			.run(ctx -> {
				assertThat(ctx).hasSingleBean(WebSocketHandler.class);
				assertThat(ctx).hasBean("otherWebSocketHandler");
				assertThat(ctx).doesNotHaveBean("webSocketHandler");
			});
	}

	@Test
	void backsOffDefaultConfigurerWhenUserWebSocketConfigurerExists() {
		this.contextRunner.withPropertyValues("ballcat.websocket.map-session=false")
			.withUserConfiguration(OtherWebSocketConfigurerConfiguration.class)
			.run(ctx -> {
				assertThat(ctx).hasSingleBean(WebSocketConfigurer.class);
				assertThat(ctx).hasBean("otherWebSocketConfigurer");
				assertThat(ctx).doesNotHaveBean("webSocketConfigurer");
			});
	}

	@Test
	void backsOffRocketMqDistributorWhenRocketMqTemplateMissing() {
		this.contextRunner.withClassLoader(new FilteredClassLoader(RocketMQTemplate.class))
			.withPropertyValues("ballcat.websocket.map-session=false", "ballcat.websocket.message-distributor=rocketmq")
			.run(ctx -> {
				assertThat(ctx).hasNotFailed();
				assertThat(ctx).doesNotHaveBean(MessageDistributor.class);
			});
	}

	@Configuration(proxyBeanMethods = false)
	static class OtherWebSocketHandlerConfiguration {

		@Bean
		WebSocketHandler otherWebSocketHandler() {
			return new TextWebSocketHandler();
		}

	}

	@Configuration(proxyBeanMethods = false)
	static class OtherWebSocketConfigurerConfiguration {

		@Bean
		WebSocketConfigurer otherWebSocketConfigurer() {
			return registry -> {
			};
		}

	}

}
