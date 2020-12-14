package com.hccake.ballcat.common.websocket.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.jetty.JettyWebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务器类型枚举
 * @author Yakir
 */
@RequiredArgsConstructor
@Getter
public enum ServerTypeEM {

	/**
	 * standard
	 */
	STANDARD("standard", StandardWebSocketSession.class),
	/**
	 * jetty
	 */
	JETTY("jetty", JettyWebSocketSession.class);

	private final String val;

	private final Class<? extends WebSocketSession> classes;

	private static final Map<String, ServerTypeEM> maps;

	static {
		maps = Arrays.stream(ServerTypeEM.values())
				.collect(Collectors.toMap(ServerTypeEM::getVal, e -> e, (old, news) -> news));
	}

	/**
	 * 根据名称 获取服务器枚举
	 * @param name
	 * @return
	 */
	public static ServerTypeEM getServerTypeByName(String name) {
		return maps.get(name);
	}

}
