package com.hccake.ballcat.common.websocket.handler.defaults;

import com.hccake.ballcat.common.websocket.config.WebSocketProperties;
import com.hccake.ballcat.common.websocket.handler.ChannelHandler;
import com.hccake.ballcat.common.websocket.handler.DataHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * 默认的websocket 处理器
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultWebSocketHandler extends AbstractWebSocketHandler {

	/**
	 * 管道处理
	 */
	private final ChannelHandler channelHandler;

	/**
	 * 数据处理
	 */
	private final DataHandler dataHandler;

	/**
	 * 属性数据
	 */
	private final WebSocketProperties webSocketProperties;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		Object key = session.getAttributes().get(webSocketProperties.getAttrKey());
		log.info("websocket connection established [{}]", key);
		if (channelHandler.getChannelByKey(key) == null) {
			// 将用户与session建立关联
			channelHandler.putChannel(key, session);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		Object key = session.getAttributes().get(webSocketProperties.getAttrKey());
		log.error("An exception has occurred in WebSocket  [{}] logout exception [{}]", key, exception);
		// remove channel
		channelHandler.removeChannel(key);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		Object key = session.getAttributes().get(webSocketProperties.getAttrKey());
		log.info("websocket connection already close [{}]", key);
		// remove channel
		channelHandler.removeChannel(key);

	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message instanceof TextMessage) {
			dataHandler.handleTextMessage(session, (TextMessage) message);
		}
		else if (message instanceof BinaryMessage) {
			dataHandler.handleBinaryMessage(session, (BinaryMessage) message);
		}
		else if (message instanceof PongMessage) {
			dataHandler.handlePongMessage(session, (PongMessage) message);
		}
		else {
			throw new IllegalStateException("Unexpected WebSocket message type: " + message);
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return webSocketProperties.getSupportPartialMessages();
	}

}
