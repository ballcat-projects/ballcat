package com.hccake.ballcat.common.websocket.handler.defaults;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.websocket.config.WebSocketProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * default handshake interceptor 需要注册成bean
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultHandshakeInterceptor implements HandshakeInterceptor {

	private final WebSocketProperties webSocketProperties;

	/**
	 * 握手前处理 可以执行一些认证
	 * @param request
	 * @param response
	 * @param wsHandler
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		// 请求路径
		String path = request.getURI().getPath();
		// http request
		HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		log.debug("default interceptor handler...");
		String key = httpServletRequest.getParameter("key");
		if (StrUtil.isEmpty(key)) {
			return false;
		}
		attributes.put(webSocketProperties.getAttrKey(), key);

		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}

}
