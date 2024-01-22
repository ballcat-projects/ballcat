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

package org.ballcat.redis.operation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.ballcat.redis.config.CachePropertiesHolder;

/**
 * @author Hccake 2019/9/2 15:19
 */
public abstract class AbstractCacheOps {

	protected AbstractCacheOps(ProceedingJoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	private final ProceedingJoinPoint joinPoint;

	/**
	 * 织入方法
	 * @return ProceedingJoinPoint
	 */
	public ProceedingJoinPoint joinPoint() {
		return this.joinPoint;
	}

	/**
	 * 检查缓存数据是否是空值
	 * @param cacheData 缓存数据
	 * @return true: 是空值
	 */
	public boolean nullValue(Object cacheData) {
		return CachePropertiesHolder.nullValue().equals(cacheData);
	}

}
