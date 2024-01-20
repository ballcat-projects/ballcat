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

package org.ballcat.datascope.test.datarule.user;

/**
 * 登录用户上下文
 *
 * @author hccake
 */
public final class LoginUserHolder {

	private LoginUserHolder() {
	}

	private static final ThreadLocal<LoginUser> LOGIN_USER = new ThreadLocal<>();

	/**
	 * 获取当前登录用户
	 */
	public static LoginUser get() {
		return LOGIN_USER.get();
	}

	/**
	 * 设置当前登录用户
	 */
	public static void set(LoginUser loginUser) {
		LOGIN_USER.set(loginUser);
	}

	/**
	 * 清除当前登录用户
	 */
	public static void remove() {
		LOGIN_USER.remove();
	}

}
