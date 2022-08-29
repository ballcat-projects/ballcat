package com.hccake.ballcat.autoconfigure.websocket.config;

import com.hccake.ballcat.autoconfigure.websocket.MessageDistributorTypeConstants;
import com.hccake.ballcat.autoconfigure.websocket.WebSocketProperties;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.common.websocket.distribute.RocketmqMessageDistributor;
import com.hccake.ballcat.common.websocket.session.WebSocketSessionStore;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ的消息分发器配置
 *
 * @author liu_yx
 * @since 0.9.0 2022年06月30日 14:11:34
 */
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.ROCKETMQ)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class RocketMqMessageDistributorConfig {

	private final WebSocketSessionStore webSocketSessionStore;

	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public RocketmqMessageDistributor messageDistributor(RocketMQTemplate template) {
		return new RocketmqMessageDistributor(webSocketSessionStore, template);
	}

}
