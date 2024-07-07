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

package org.ballcat.apisignature;

import java.util.concurrent.TimeUnit;

/**
 * Nonce 随机字符串存储器。
 *
 * @author hccake
 * @since 2.0.0
 */
public interface NonceStore {

	/**
	 * 存储随机字符串，如果其不存在的话。
	 * @param nonce 随机字符串
	 * @param timeout 存储的过期时长
	 * @param timeUnit 过期时长的单位
	 * @return 如果当前随机字符串已存在，则返回 false.
	 */
	boolean storeIfAbsent(String nonce, Long timeout, TimeUnit timeUnit);

}
