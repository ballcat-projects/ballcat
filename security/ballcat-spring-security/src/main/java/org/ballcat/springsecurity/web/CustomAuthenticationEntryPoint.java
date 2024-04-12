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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.model.result.SystemResultCode;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.springsecurity.exception.InternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Hccake 2019/9/25 22:04
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException {

		String utf8 = StandardCharsets.UTF_8.toString();

		httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		httpServletResponse.setCharacterEncoding(utf8);

		if (e instanceof InternalServiceException) {
			httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			ApiResult<Object> r = ApiResult.failed(SystemResultCode.SERVER_ERROR, e.getMessage());
			httpServletResponse.getWriter().write(JsonUtils.toJson(r));
		}
		else {
			httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			ApiResult<Object> r = ApiResult.failed(SystemResultCode.UNAUTHORIZED, e.getMessage());
			httpServletResponse.getWriter().write(JsonUtils.toJson(r));
		}
	}

}
