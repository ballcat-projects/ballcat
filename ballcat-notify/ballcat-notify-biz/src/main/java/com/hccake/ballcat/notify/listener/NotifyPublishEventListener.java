package com.hccake.ballcat.notify.listener;

import com.hccake.ballcat.notify.event.NotifyPublishEvent;
import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.notify.push.NotifyPushExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 通知发布事件监听器
 *
 * @author Hccake 2020/12/17
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyPublishEventListener {

	private final NotifyPushExecutor notifyPushExecutor;

	/**
	 * 通知发布事件
	 * @param event the NotifyPublishEvent
	 */
	@Async
	@EventListener(NotifyPublishEvent.class)
	public void onNotifyPublishEvent(NotifyPublishEvent event) {
		NotifyInfo notifyInfo = event.getNotifyInfo();
		// 推送通知
		notifyPushExecutor.push(notifyInfo);
	}

}
