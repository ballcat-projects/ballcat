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

package org.ballcat.autoconfigure.redis;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.ballcat.redis.listener.MessageEventListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author hccake
 */
@RequiredArgsConstructor
public class AddMessageEventListenerToContainer {

	private final RedisMessageListenerContainer listenerContainer;

	private final List<MessageEventListener> listenerList;

	/**
	 * 将所有的 <code>MessageEventListener<code/> 注册到
	 * <code>RedisMessageListenerContainer<code/> 中
	 */
	@PostConstruct
	public void addMessageListener() {
		// 注册监听器
		for (MessageEventListener messageEventListener : this.listenerList) {
			this.listenerContainer.addMessageListener(messageEventListener, messageEventListener.topic());
		}
	}

}
