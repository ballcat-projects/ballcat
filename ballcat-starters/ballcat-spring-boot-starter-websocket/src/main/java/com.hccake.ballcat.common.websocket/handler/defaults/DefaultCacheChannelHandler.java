package com.hccake.ballcat.common.websocket.handler.defaults;

import com.hccake.ballcat.common.core.util.JacksonUtils;
import com.hccake.ballcat.common.websocket.config.WebSocketProperties;
import com.hccake.ballcat.common.websocket.enums.ServerTypeEM;
import com.hccake.ballcat.common.websocket.function.SessionFunction;
import com.hccake.ballcat.common.websocket.handler.ChannelHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * redis 缓存 状态管理 默认实现 不支持jetty
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultCacheChannelHandler implements ChannelHandler<String, WebSocketSession> {

	/**
	 * redis hash key
	 */
	private static final String redisOnlineKey = "ws:cache:user:online";

	private final WebSocketProperties webSocketProperties;

	private final RedisTemplate redisTemplate;

	@Override
	public WebSocketSession getChannelByKey(String key) {
		return getWebSocketSessionByKey(key);
	}

	@Override
	public void putChannel(String key, WebSocketSession webSocketSession) {
		redisTemplate.opsForHash().put(redisOnlineKey, key, JacksonUtils.toJson(webSocketSession));
	}

	@Override
	public void removeChannel(String key) {
		if (redisTemplate.opsForHash().hasKey(redisOnlineKey, key)) {
			redisTemplate.opsForHash().delete(redisOnlineKey, key);
		}
	}

	/**
	 * key to po
	 * @param key
	 * @return
	 */
	private WebSocketSession getWebSocketSessionByKey(String key) {
		String sessionJson = (String) redisTemplate.opsForHash().get(redisOnlineKey, key);
		return sessionJson == null ? null : convertToWebSocketSession(sessionJson);
	}

	/**
	 * json to po
	 * @param sessionJson
	 * @return
	 */
	private WebSocketSession convertToWebSocketSession(String sessionJson) {
		String serverType = webSocketProperties.getServerType();
		ServerTypeEM serverTypeEM = ServerTypeEM.getServerTypeByName(serverType);
		if (serverTypeEM == null) {
			throw new IllegalArgumentException("cache session convert fail");
		}
		return JacksonUtils.toObj(sessionJson, serverTypeEM.getClasses());
	}

	@Override
	public List<WebSocketSession> getChannels() {
		Map<String, String> map = redisTemplate.opsForHash().entries(redisOnlineKey);
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		return map.values().stream().map(e -> convertToWebSocketSession(e)).collect(Collectors.toList());
	}

	@Override
	public boolean isOpen(WebSocketSession webSocketSession) {
		return webSocketSession != null && webSocketSession.isOpen();
	}

}
