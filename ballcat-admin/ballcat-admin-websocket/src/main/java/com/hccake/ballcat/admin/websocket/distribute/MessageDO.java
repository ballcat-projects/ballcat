package com.hccake.ballcat.admin.websocket.distribute;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class MessageDO {

	/**
	 * 是否广播
	 */
	private Boolean needBroadcast;

	/**
	 * sessionKeys
	 */
	private List<Object> sessionKeys;

	/**
	 * 需要发送的消息文本
	 */
	private String messageText;

}
