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

package org.ballcat.datascope.holder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DataScope 匹配数
 *
 * @author hccake
 */
public final class DataScopeMatchNumHolder {

	private DataScopeMatchNumHolder() {
	}

	private static final ThreadLocal<Deque<AtomicInteger>> matchNumTreadLocal = new ThreadLocal<>();

	/**
	 * 每次 SQL 执行解析前初始化匹配次数为 0
	 */
	public static void initMatchNum() {
		Deque<AtomicInteger> deque = matchNumTreadLocal.get();
		if (deque == null) {
			deque = new ArrayDeque<>();
			matchNumTreadLocal.set(deque);
		}
		deque.push(new AtomicInteger());
	}

	/**
	 * 获取当前 SQL 解析后被数据权限匹配中的次数
	 * @return int 次数
	 */
	public static Integer pollMatchNum() {
		Deque<AtomicInteger> deque = matchNumTreadLocal.get();
		AtomicInteger matchNum = deque.poll();
		return matchNum == null ? null : matchNum.get();
	}

	/**
	 * 如果存在计数器，则次数 +1
	 */
	public static void incrementMatchNumIfPresent() {
		Deque<AtomicInteger> deque = matchNumTreadLocal.get();
		Optional.ofNullable(deque).map(Deque::peek).ifPresent(AtomicInteger::incrementAndGet);
	}

	/**
	 * 删除 matchNumTreadLocal，在 SQL 执行解析后调用
	 */
	public static void removeIfEmpty() {
		Deque<AtomicInteger> deque = matchNumTreadLocal.get();
		if (deque == null || deque.isEmpty()) {
			matchNumTreadLocal.remove();
		}
	}

}
