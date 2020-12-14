package com.hccake.ballcat.common.websocket.handler.defaults;

import com.hccake.ballcat.common.websocket.handler.DataHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 默认的消息数据处理器
 *
 * @author Yakir
 */
@Slf4j
public class DefaultDataHandler implements DataHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		log.debug("receive a text data [{}]", message);
	}

	@Override
	public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		log.debug("receive a binary data [{}]", message);
	}

	@Override
	public void handlePongMessage(WebSocketSession session, PongMessage message) {
		log.debug("receive a pong data [{}]", message);
	}

}
