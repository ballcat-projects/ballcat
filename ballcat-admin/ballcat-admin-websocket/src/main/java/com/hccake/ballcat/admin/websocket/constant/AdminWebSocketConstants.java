package com.hccake.ballcat.admin.websocket.constant;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
public final class AdminWebSocketConstants {

	private AdminWebSocketConstants() {
	}

	/**
	 * 存储在 WebSocketSession Attribute 中的 token 属性名
	 */
	public static final String TOKEN_ATTR_NAME = OAuth2AccessToken.ACCESS_TOKEN;

	/**
	 * 存储在 WebSocketSession Attribute 中的 用户唯一标识 属性名
	 */
	public static final String USER_KEY_ATTR_NAME = "userId";

}
