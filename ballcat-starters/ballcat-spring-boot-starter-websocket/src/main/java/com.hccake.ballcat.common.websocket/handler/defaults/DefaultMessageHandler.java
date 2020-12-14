package com.hccake.ballcat.common.websocket.handler.defaults;

import com.hccake.ballcat.common.websocket.function.SessionFunction;
import com.hccake.ballcat.common.websocket.handler.ChannelHandler;
import com.hccake.ballcat.common.websocket.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * 消息处理
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultMessageHandler implements MessageHandler<String, WebSocketSession> {

	private final ChannelHandler channelHandler;

	@Override
	public <T extends WebSocketMessage> void broadCastMsg(T msg) {
		List<WebSocketSession> webSocketSessions = channelHandler.getChannels();
		for (WebSocketSession session : webSocketSessions) {
			try {
				if (session.isOpen()) {
					session.sendMessage(msg);
				}
			}
			catch (IOException e) {
				log.error("An exception has occurred in the broadcast message message[{}]", msg, e);
			}
		}
	}

	@Override
	public void broadTextCastMsg(String msg) {
		broadCastMsg(new TextMessage(msg));
	}

	@Override
	public <T extends WebSocketMessage> boolean sendMsg(String key, T msg) {
		WebSocketSession session = channelHandler.getChannelByKey(key);
		if (session != null && session.isOpen()) {
			try {
				session.sendMessage(msg);
				return true;
			}
			catch (IOException e) {
				log.error("An exception has occurred in the send message key [{}] message[{}]", key, msg, e);
			}
		}
		return false;
	}

	@Override
	public boolean sendTextMsg(String key, String msg) {
		return sendMsg(key, new TextMessage(msg));
	}

	@Override
	public boolean sendMsg(String key, SessionFunction<WebSocketSession, Boolean> consumer) {
		WebSocketSession session = channelHandler.getChannelByKey(key);
		if (session != null && session.isOpen()) {
			try {
				return consumer.apply(session);
			}
			catch (Exception e) {
				log.error("send msg error", e);
			}

		}
		return false;
	}

	@Override
	public boolean sendMsg(Integer key, Consumer<WebSocketSession> consumer) {
		WebSocketSession session = channelHandler.getChannelByKey(key);
		if (session != null && session.isOpen()) {
			try {
				consumer.accept(session);
				return true;
			}
			catch (Exception e) {
				log.error("An exception has occurred in the send message ", e);
				return false;
			}
		}
		return false;
	}

}
