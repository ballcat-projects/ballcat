package com.hccake.ballcat.notify.model.dto;

import com.hccake.ballcat.common.websocket.message.AbstractJsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 公告发布消息
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Getter
@Setter
public class AnnouncementPushMessage extends AbstractJsonWebSocketMessage {

	public AnnouncementPushMessage() {
		super("announcement-push");
	}

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 永久有效的
	 * @see com.hccake.ballcat.common.core.constant.enums.BooleanEnum
	 */
	private Integer immortal;

	/**
	 * 截止日期
	 */
	private LocalDateTime deadline;

}
