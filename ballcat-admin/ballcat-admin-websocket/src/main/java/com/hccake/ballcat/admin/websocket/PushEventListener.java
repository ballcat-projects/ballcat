package com.hccake.ballcat.admin.websocket;

import com.hccake.ballcat.admin.modules.notify.event.StationNotifyPushEvent;
import com.hccake.ballcat.admin.modules.notify.model.domain.NotifyInfo;
import com.hccake.ballcat.admin.modules.sys.event.DictChangeEvent;
import com.hccake.ballcat.admin.modules.sys.model.entity.SysUser;
import com.hccake.ballcat.admin.websocket.message.DictChangeMessage;
import com.hccake.ballcat.admin.websocket.message.PushAnnouncementMessage;
import com.hccake.ballcat.common.core.util.JacksonUtils;
import com.hccake.ballcat.common.websocket.WebSocketMessageSender;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Component
public class PushEventListener {

	/**
	 * 字典修改事件监听
	 * @param event the DictChangeEvent
	 */
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
	 * 站内通知推送事件
	 * @param event the StationNotifyPushEvent
	 */
	@EventListener(StationNotifyPushEvent.class)
	public void onAnnouncementPublishEvent(StationNotifyPushEvent event) {
		NotifyInfo notifyInfo = event.getNotifyInfo();
		List<SysUser> userList = event.getUserList();

		// 构建发布公告的消息体
		PushAnnouncementMessage message = new PushAnnouncementMessage();
		message.setTitle(notifyInfo.getTitle());
		message.setContent(notifyInfo.getContent());
		String msg = JacksonUtils.toJson(message);

		// 向指定用户推送
		for (SysUser sysUser : userList) {
			WebSocketMessageSender.send(sysUser.getUserId(), msg);
		}
	}

}
