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

package org.ballcat.springsecurity.oauth2.server.authorization.web.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * OAuth2 撤销令牌成功的处理，发布登出事件，以及响应 200
 *
 * @author hccake
 * @link <a target="_blank" href="https://tools.ietf.org/html/rfc7009#section-2">Section 2
 * Token Revocation</a>
 * @link <a target="_blank" href="https://tools.ietf.org/html/rfc7009#section-2.1">Section
 * 2.1 Revocation Request</a>
 */
@RequiredArgsConstructor
public class OAuth2TokenRevocationResponseHandler implements AuthenticationSuccessHandler {

	private final ApplicationEventPublisher publisher;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		// 发布用户登出事件
		this.publisher.publishEvent(new LogoutSuccessEvent(authentication));
		// 返回 200 响应
		response.setStatus(HttpStatus.OK.value());
	}

}
