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

package org.ballcat.websocket.message;

/**
 * <p>
 * BallCat 自定义的 Json 类型的消息
 * </p>
 *
 * <ul>
 * <li>要求消息内容必须是一个 Json 对象
 * <li>Json 对象中必须有一个属性 type
 * <ul/>
 *
 * @author Hccake 2021/1/4
 *
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
public abstract class JsonWebSocketMessage {

	public static final String TYPE_FIELD = "type";

	private final String type;

	protected JsonWebSocketMessage(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

}
