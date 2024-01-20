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

package org.ballcat.springsecurity.oauth2;

/**
 * @author hccake
 */
public final class ScopeNames {

	private ScopeNames() {
	}

	/**
	 * 跳过验证码
	 */
	public static final String SKIP_CAPTCHA = "skip_captcha";

	/**
	 * 跳过密码解密 （使用明文传输）
	 */
	public static final String SKIP_PASSWORD_DECODE = "skip_password_decode";

}
