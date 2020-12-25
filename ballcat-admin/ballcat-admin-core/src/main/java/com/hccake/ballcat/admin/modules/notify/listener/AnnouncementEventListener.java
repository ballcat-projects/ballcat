package com.hccake.ballcat.admin.modules.notify.listener;

import com.hccake.ballcat.admin.modules.notify.event.NotifyPublishEvent;
import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.notify.push.NotifyPushExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 公告事件监听器
 *
 * @author Hccake 2020/12/17
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AnnouncementEventListener {

	private final NotifyPushExecutor notifyPushExecutor;

	/**
	 * 通知发布事件
	 * @param event the NotifyPublishEvent
	 */
	@EventListener(NotifyPublishEvent.class)
	public void onAnnouncementPublishEvent(NotifyPublishEvent event) {
		NotifyInfo notifyInfo = event.getNotifyInfo();
		// 推送通知
		notifyPushExecutor.push(notifyInfo);
	}

}
