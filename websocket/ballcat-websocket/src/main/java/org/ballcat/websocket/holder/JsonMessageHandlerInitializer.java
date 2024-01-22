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

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.ballcat.websocket.handler.JsonMessageHandler;
import org.ballcat.websocket.message.JsonWebSocketMessage;

/**
 * <p>
 * JsonMessageHandler 初始化器
 * <p/>
 * 将所有的 jsonMessageHandler 收集到 JsonMessageHandlerHolder 中
 *
 * @author Hccake
 */
@RequiredArgsConstructor
public class JsonMessageHandlerInitializer {

	private final List<JsonMessageHandler<? extends JsonWebSocketMessage>> jsonMessageHandlerList;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler<? extends JsonWebSocketMessage> jsonMessageHandler : this.jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler((JsonMessageHandler<JsonWebSocketMessage>) jsonMessageHandler);
		}
	}

}
