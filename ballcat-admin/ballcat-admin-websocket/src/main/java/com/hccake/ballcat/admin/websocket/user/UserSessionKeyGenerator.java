package com.hccake.ballcat.admin.websocket.user;

import com.hccake.ballcat.admin.websocket.constant.AdminWebSocketConstants;
import com.hccake.ballcat.common.websocket.holder.SessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

/**
 * <p>
 * 用户 WebSocketSession 唯一标识生成器
 * </p>
 *
 * 此类主要使用当前 session 对应用户的唯一标识做为 session 的唯一标识 方便系统快速通过用户获取对应 session
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@RequiredArgsConstructor
public class UserSessionKeyGenerator implements SessionKeyGenerator {

	/**
	 * 获取当前session的唯一标识，用户的唯一标识已经通过
	 * @see UserAttributeHandshakeInterceptor 存储在当前 session 的属性中
	 * @param webSocketSession 当前session
	 * @return session唯一标识
	 */
	@Override
	public Object sessionKey(WebSocketSession webSocketSession) {
		return webSocketSession.getAttributes().get(AdminWebSocketConstants.USER_KEY_ATTR_NAME);
	}

}
