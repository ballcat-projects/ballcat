/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.websocket.session;

import java.util.Collection;

import org.springframework.web.socket.WebSocketSession;

/**
 * @author hccake
 */
public interface WebSocketSessionStore {

	/**
	 * 添加一个 session
	 * @param session 待添加的 WebSocketSession
	 */
	void addSession(WebSocketSession session);

	/**
	 * 删除一个 session
	 * @param session WebSocketSession
	 */
	void removeSession(WebSocketSession session) throws Exception;

	/**
	 * 获取当前所有在线的 wsSessions
	 * @return Collection<WebSocketSession> websocket session集合
	 */
	Collection<WebSocketSession> getSessions();

	/**
	 * 根据指定的 sessionKey 获取对应的 wsSessions
	 * @param sessionKey wsSession 标识
	 * @return Collection<WebSocketSession> websocket session集合
	 */
	Collection<WebSocketSession> getSessions(Object sessionKey);

	/**
	 * 获取所有的 sessionKeys
	 * @return sessionKey 集合
	 */
	Collection<Object> getSessionKeys();

}
