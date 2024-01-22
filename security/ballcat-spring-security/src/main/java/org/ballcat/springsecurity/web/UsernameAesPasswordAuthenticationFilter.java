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

package org.ballcat.springsecurity.web;

import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;

import org.ballcat.springsecurity.util.PasswordUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 允许前后端交互时使用 AES 加密的传输密码的用户密码认证过滤器
 *
 * @author Hccake
 * @since 2.0.0
 */
public class UsernameAesPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final String passwordSecretKey;

	public UsernameAesPasswordAuthenticationFilter() {
		this(null);
	}

	public UsernameAesPasswordAuthenticationFilter(String passwordSecretKey) {
		this.passwordSecretKey = passwordSecretKey;
	}

	public UsernameAesPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
			String passwordSecretKey) {
		super(authenticationManager);
		this.passwordSecretKey = passwordSecretKey;
	}

	@Nullable
	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String requestPassword = request.getParameter(getPasswordParameter());
		if (this.passwordSecretKey == null) {
			return requestPassword;
		}

		// 先进行 AES 解密
		try {
			return PasswordUtils.decodeAES(requestPassword, this.passwordSecretKey);
		}
		catch (GeneralSecurityException e) {
			throw new IllegalArgumentException("error parameter", e);
		}
	}

}
