package com.hccake.ballcat.notify.websocket;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.websocket.distribute.MessageDO;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.notify.event.AnnouncementCloseEvent;
import com.hccake.ballcat.notify.event.StationNotifyPushEvent;
import com.hccake.ballcat.notify.handler.NotifyInfoDelegateHandler;
import com.hccake.ballcat.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.notify.model.dto.AnnouncementCloseMessage;
import com.hccake.ballcat.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class NotifyWebsocketEventListener {

	private final MessageDistributor messageDistributor;

	private final NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler;

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
		String msg = JsonUtils.toJson(message);

		// 广播修改信息
		MessageDO messageDO = new MessageDO().setMessageText(msg).setNeedBroadcast(true);
		messageDistributor.distribute(messageDO);
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
		notifyInfoDelegateHandler.handle(userList, notifyInfo);
	}

}
