package com.hccake.ballcat.admin.websocket.message;

import com.hccake.ballcat.common.websocket.message.AbstractJsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典修改消息
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Getter
@Setter
public class DictChangeMessage extends AbstractJsonWebSocketMessage {

	public DictChangeMessage() {
		super("dict-change");
	}

	/**
	 * 改动的字典标识
	 */
	private String dictCode;

}
