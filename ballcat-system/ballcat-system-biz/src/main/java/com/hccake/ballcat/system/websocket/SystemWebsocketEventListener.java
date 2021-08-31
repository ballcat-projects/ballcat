package com.hccake.ballcat.system.websocket;

import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.websocket.distribute.MessageDO;
import com.hccake.ballcat.common.websocket.distribute.MessageDistributor;
import com.hccake.ballcat.system.event.DictChangeEvent;
import com.hccake.ballcat.system.event.LovChangeEvent;
import com.hccake.ballcat.system.model.dto.DictChangeMessage;
import com.hccake.ballcat.system.model.dto.LovChangeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class SystemWebsocketEventListener {

	private final MessageDistributor messageDistributor;

	@Async
	@EventListener(LovChangeEvent.class)
	public void onLovChangeEvent(LovChangeEvent event) {
		LovChangeMessage message = LovChangeMessage.of(event.getKeyword());
		messageDistributor.distribute(MessageDO.broadcastMessage(message.toString()));
	}

	/**
	 * 字典修改事件监听
	 * @param event the `DictChangeEvent`
	 */
	@Async
	@EventListener(DictChangeEvent.class)
	public void onDictChangeEvent(DictChangeEvent event) {
		// 构建字典修改的消息体
		DictChangeMessage dictChangeMessage = new DictChangeMessage();
		dictChangeMessage.setDictCode(event.getDictCode());
		String msg = JsonUtils.toJson(dictChangeMessage);

		// 广播修改信息
		MessageDO messageDO = new MessageDO().setMessageText(msg).setNeedBroadcast(true);
		messageDistributor.distribute(messageDO);
	}

}
