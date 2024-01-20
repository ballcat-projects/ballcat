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

package org.ballcat.xss.core;

/**
 * Xss状态持有者
 *
 * @author Hccake 2021/3/16
 *
 */
public final class XssStateHolder {

	private XssStateHolder() {
	}

	private static final ThreadLocal<Boolean> STATE = new ThreadLocal<>();

	/**
	 * 打开 Xss 过滤
	 */
	public static void open() {
		STATE.set(Boolean.TRUE);
	}

	/**
	 * Xss 过滤是否开启
	 * @return 开启：true
	 */
	public static boolean enabled() {
		return Boolean.TRUE.equals(STATE.get());
	}

	/**
	 * 删除状态
	 */
	public static void remove() {
		STATE.remove();
	}

}
