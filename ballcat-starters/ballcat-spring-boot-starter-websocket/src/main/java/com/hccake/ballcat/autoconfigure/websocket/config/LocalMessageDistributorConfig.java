package com.hccake.ballcat.autoconfigure.websocket.config;

import com.hccake.ballcat.autoconfigure.websocket.MessageDistributorTypeConstants;
import com.hccake.ballcat.autoconfigure.websocket.WebSocketProperties;
import com.hccake.ballcat.common.websocket.distribute.LocalMessageDistributor;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.common.websocket.session.WebSocketSessionStore;
import lombok.RequiredArgsConstructor;
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
		return new LocalMessageDistributor(webSocketSessionStore);
	}

}
