package com.hccake.ballcat.admin.websocket.message;

import com.hccake.ballcat.common.websocket.message.AbstractJsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 公告发布消息
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Getter
@Setter
public class PushAnnouncementMessage extends AbstractJsonWebSocketMessage {

	public PushAnnouncementMessage() {
		super("push-announcement");
	}

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

}
