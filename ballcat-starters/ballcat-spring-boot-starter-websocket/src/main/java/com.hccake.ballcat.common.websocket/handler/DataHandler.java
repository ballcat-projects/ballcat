package com.hccake.ballcat.common.websocket.handler;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 消息数据处理
 *
 * @author Yakir
 */
public interface DataHandler {

	/**
	 * 处理文本消息
	 * @param session
	 * @param message
	 */
	default void handleTextMessage(WebSocketSession session, TextMessage message) {

	};

	/**
	 * 处理二进制消息
	 * @param session
	 * @param message
	 */
	default void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {

	};

	/**
	 * 处理心跳消息
	 * @param session
	 * @param message
	 */
	default void handlePongMessage(WebSocketSession session, PongMessage message) {
	};

}
