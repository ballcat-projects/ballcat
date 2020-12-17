package com.hccake.ballcat.admin.modules.notify.listener;

import com.hccake.ballcat.admin.modules.notify.event.AnnouncementPublishEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 公告事件监听器
 *
 * @author Hccake 2020/12/17
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AnnouncementEventListener {

	/**
	 * 公告发布事件处理
	 * @param event the AnnouncementPublishEvent
	 */
	@EventListener(AnnouncementPublishEvent.class)
	public void onAnnouncementPublishEvent(AnnouncementPublishEvent event) {
		// TODO 公告通知
		System.out.println(event.getAnnouncement());
	}

}
