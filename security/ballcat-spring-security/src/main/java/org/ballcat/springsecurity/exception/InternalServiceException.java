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

package org.ballcat.springsecurity.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 认证时发生内部服务异常。
 * <p>
 *
 * @see org.springframework.security.authentication.InternalAuthenticationServiceException
 * @author Hccake
 * @since 2.0.0
 */
public class InternalServiceException extends AuthenticationException {

	public InternalServiceException(String message) {
		super(message);
	}

	public InternalServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
