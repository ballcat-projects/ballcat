package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.system.websocket.SystemWebsocketEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnClass(SystemWebsocketEventListener.class)
@Configuration(proxyBeanMethods = false)
public class SystemWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public SystemWebsocketEventListener systemWebsocketEventListener() {
		return new SystemWebsocketEventListener(messageDistributor);
	}

}
