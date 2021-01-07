package com.hccake.ballcat.admin.websocket.message;

import com.hccake.ballcat.common.websocket.message.AbstractJsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hccake 2021/1/7
 * @version 1.0
 */
@Getter
@Setter
public class AnnouncementCloseMessage extends AbstractJsonWebSocketMessage {

	public AnnouncementCloseMessage() {
		super("announcement-close");
	}

	/**
	 * ID
	 */
	private Long id;

}
