package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.notify.service.UserAnnouncementService;
import com.hccake.ballcat.notify.websocket.NotifyWebsocketEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnClass({ NotifyWebsocketEventListener.class, UserAnnouncementService.class })
@Configuration(proxyBeanMethods = false)
public class NotifyWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public NotifyWebsocketEventListener notifyWebsocketEventListener(UserAnnouncementService userAnnouncementService) {
		return new NotifyWebsocketEventListener(userAnnouncementService, messageDistributor);
	}

}
