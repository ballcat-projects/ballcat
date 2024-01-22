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

import java.util.function.Consumer;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake 2019/9/2 15:19
 */
public class CachePutOps extends AbstractCacheOps {

	/**
	 * 向缓存写入数据
	 */
	private final Consumer<Object> cachePut;

	public CachePutOps(ProceedingJoinPoint joinPoint, Consumer<Object> cachePut) {
		super(joinPoint);
		this.cachePut = cachePut;
	}

	public Consumer<Object> cachePut() {
		return this.cachePut;
	}

}
