package com.hccake.ballcat.admin.modules.notify.listener;

import com.hccake.ballcat.admin.modules.notify.event.AnnouncementPublishEvent;
import com.hccake.ballcat.admin.modules.notify.model.entity.Announcement;
import com.hccake.ballcat.admin.modules.notify.push.NotifyPushRunner;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

	private final NotifyPushRunner notifyPusherRunner;

	/**
	 * // TODO 同步处理，失败回滚 公告发布事件处理
	 * @param event the AnnouncementPublishEvent
	 */
	@EventListener(AnnouncementPublishEvent.class)
	public void onAnnouncementPublishEvent(AnnouncementPublishEvent event) {
		Announcement announcement = event.getAnnouncement();

		// TODO 根据接收人条件筛选
		List<SysUser> userList = new ArrayList<>();
		SysUser sysUser = new SysUser();
		sysUser.setEmail("chengbohua@foxmail.com");
		userList.add(sysUser);

		// 推送通知
		notifyPusherRunner.run(announcement, userList, announcement.getReceiveMode());
	}

}
