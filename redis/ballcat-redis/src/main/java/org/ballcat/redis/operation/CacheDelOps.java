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
import org.ballcat.redis.operation.function.VoidMethod;

/**
 * @author Hccake 2019/9/2 15:19
 */
public class CacheDelOps extends AbstractCacheOps {

	/**
	 * 删除缓存数据
	 */
	private final VoidMethod cacheDel;

	public CacheDelOps(ProceedingJoinPoint joinPoint, VoidMethod cacheDel) {
		super(joinPoint);
		this.cacheDel = cacheDel;
	}

	public VoidMethod cacheDel() {
		return this.cacheDel;
	}

}
