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

package org.ballcat.redis.keyevent.template;

import org.springframework.lang.Nullable;

/**
 * common key event message template
 *
 * @author lishangbu 2023/1/12
 */
public interface KeyEventMessageTemplate {

	/**
	 * handle actual message with notified key
	 * @param key notified key from redis server
	 */
	void handleMessage(@Nullable String key);

	/**
	 * support this template or not
	 * @param key notified key from redis server
	 * @return {@code true} is support and {@code false} is not support
	 */
	boolean support(@Nullable String key);

}
