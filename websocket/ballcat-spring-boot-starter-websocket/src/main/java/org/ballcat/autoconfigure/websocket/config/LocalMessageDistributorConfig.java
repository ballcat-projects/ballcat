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

package org.ballcat.autoconfigure.websocket.config;

import lombok.RequiredArgsConstructor;
import org.ballcat.autoconfigure.websocket.MessageDistributorTypeConstants;
import org.ballcat.autoconfigure.websocket.WebSocketProperties;
import org.ballcat.websocket.distribute.LocalMessageDistributor;
import org.ballcat.websocket.distribute.MessageDistributor;
import org.ballcat.websocket.session.WebSocketSessionStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地的消息分发器配置
 *
 * @author hccake
 */
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.LOCAL, matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LocalMessageDistributorConfig {

	private final WebSocketSessionStore webSocketSessionStore;

	/**
	 * 本地基于内存的消息分发，不支持集群
	 * @return LocalMessageDistributor
	 */
	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public LocalMessageDistributor messageDistributor() {
		return new LocalMessageDistributor(this.webSocketSessionStore);
	}

}
