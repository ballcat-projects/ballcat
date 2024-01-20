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

package org.ballcat.idempotent.key.store;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 幂等Key存储
 * </p>
 *
 * 消费过的幂等 key 记录下来，再下次消费前校验 key 是否已记录，从而拒绝执行
 *
 * @author hccake
 */
public interface IdempotentKeyStore {

	/**
	 * 当不存在有效 key 时将其存储下来
	 * @param key idempotentKey
	 * @param duration key的有效时长
	 * @param timeUnit 时长单位
	 * @return boolean true: 存储成功 false: 存储失败
	 */
	boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit);

	/**
	 * 删除 key
	 * @param key idempotentKey
	 */
	void remove(String key);

}
