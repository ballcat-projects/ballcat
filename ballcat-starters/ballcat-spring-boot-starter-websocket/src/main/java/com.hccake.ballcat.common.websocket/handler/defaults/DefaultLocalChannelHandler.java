package com.hccake.ballcat.common.websocket.handler.defaults;

import com.hccake.ballcat.common.websocket.function.SessionFunction;
import com.hccake.ballcat.common.websocket.handler.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 默认的消息管道服务实现 本地缓存
 *
 * @author Yakir
 */
@Slf4j
public class DefaultLocalChannelHandler implements ChannelHandler<String, WebSocketSession> {

	/**
	 * 管道集合
	 */
	private static final ConcurrentHashMap<String, WebSocketSession> channels = new ConcurrentHashMap<>();

	@Override
	public WebSocketSession getChannelByKey(String key) {
		return channels.get(key);
	}

	@Override
	public void putChannel(String key, WebSocketSession webSocketSession) {
		channels.put(key, webSocketSession);
	}

	@Override
	public void removeChannel(String key) {
		if (channels.containsKey(key)) {
			channels.remove(key);
		}
	}

	@Override
	public List<WebSocketSession> getChannels() {
		return channels.values().stream().collect(Collectors.toList());
	}

	@Override
	public boolean isOpen(WebSocketSession webSocketSession) {
		return webSocketSession != null && webSocketSession.isOpen();
	}

}
