package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.admin.modules.notify.event.AnnouncementCloseEvent;
import com.hccake.ballcat.admin.modules.notify.event.StationNotifyPushEvent;
import com.hccake.ballcat.admin.modules.notify.model.domain.AnnouncementNotifyInfo;
import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.notify.model.entity.UserAnnouncement;
import com.hccake.ballcat.admin.modules.notify.service.UserAnnouncementService;
import com.hccake.ballcat.admin.modules.sys.event.DictChangeEvent;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.websocket.message.AnnouncementCloseMessage;
import com.hccake.ballcat.admin.websocket.message.AnnouncementPushMessage;
import com.hccake.ballcat.admin.websocket.message.DictChangeMessage;
import com.hccake.ballcat.common.core.util.JacksonUtils;
import com.hccake.ballcat.common.websocket.WebSocketMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PushEventListener {

	private final UserAnnouncementService userAnnouncementService;

	/**
	 * 字典修改事件监听
	 * @param event the DictChangeEvent
	 */
	@Async
	@EventListener(DictChangeEvent.class)
	public void onDictChangeEvent(DictChangeEvent event) {
		// 构建字典修改的消息体
		DictChangeMessage dictChangeMessage = new DictChangeMessage();
		dictChangeMessage.setDictCode(event.getDictCode());
		String msg = JacksonUtils.toJson(dictChangeMessage);

		// 广播修改信息
		WebSocketMessageSender.broadcast(msg);
	}

	/**
	 * 公告关闭事件监听
	 * @param event the AnnouncementCloseEvent
	 */
	@Async
	@EventListener(AnnouncementCloseEvent.class)
	public void onAnnouncementCloseEvent(AnnouncementCloseEvent event) {
		// 构建字典修改的消息体
		AnnouncementCloseMessage message = new AnnouncementCloseMessage();
		message.setId(event.getId());
		String msg = JacksonUtils.toJson(message);

		// 广播修改信息
		WebSocketMessageSender.broadcast(msg);
	}

	/**
	 * 站内通知推送事件
	 * @param event the StationNotifyPushEvent
	 */
	@Async
	@EventListener(StationNotifyPushEvent.class)
	public void onAnnouncementPublishEvent(StationNotifyPushEvent event) {
		NotifyInfo notifyInfo = event.getNotifyInfo();
		List<SysUser> userList = event.getUserList();

		// TODO 暂时只有公告通知，后续添加提醒类型通知
		if (notifyInfo instanceof AnnouncementNotifyInfo) {
			AnnouncementNotifyInfo announcementNotifyInfo = (AnnouncementNotifyInfo) notifyInfo;
			// 构建发布公告的消息体
			AnnouncementPushMessage message = new AnnouncementPushMessage();
			message.setId(announcementNotifyInfo.getId());
			message.setTitle(announcementNotifyInfo.getTitle());
			message.setContent(announcementNotifyInfo.getContent());
			message.setImmortal(announcementNotifyInfo.getImmortal());
			message.setDeadline(announcementNotifyInfo.getDeadline());
			String msg = JacksonUtils.toJson(message);

			List<UserAnnouncement> userAnnouncements = new ArrayList<>();
			// 向指定用户推送
			for (SysUser sysUser : userList) {
				Integer userId = sysUser.getUserId();
				boolean send = WebSocketMessageSender.send(userId, msg);
				if (send) {
					UserAnnouncement userAnnouncement = userAnnouncementService.prodUserAnnouncement(userId,
							announcementNotifyInfo.getId());
					userAnnouncements.add(userAnnouncement);
				}
			}
			userAnnouncementService.saveBatch(userAnnouncements);
		}
	}

}
