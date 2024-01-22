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

package org.ballcat.websocket.holder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.websocket.handler.JsonMessageHandler;
import org.ballcat.websocket.message.JsonWebSocketMessage;

/**
 * @author Hccake 2021/1/4
 *
 */
public final class JsonMessageHandlerHolder {

	private JsonMessageHandlerHolder() {
	}

	private static final Map<String, JsonMessageHandler<JsonWebSocketMessage>> MESSAGE_HANDLER_MAP = new ConcurrentHashMap<>();

	public static JsonMessageHandler<JsonWebSocketMessage> getHandler(String type) {
		return MESSAGE_HANDLER_MAP.get(type);
	}

	public static void addHandler(JsonMessageHandler<JsonWebSocketMessage> jsonMessageHandler) {
		MESSAGE_HANDLER_MAP.put(jsonMessageHandler.type(), jsonMessageHandler);
	}

}
