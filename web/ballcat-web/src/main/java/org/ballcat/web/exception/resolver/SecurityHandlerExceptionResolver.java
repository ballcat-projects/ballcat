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

package org.ballcat.web.exception.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.model.result.SystemResultCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hccake
 */
@Order(-1000)
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class SecurityHandlerExceptionResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	/**
	 * AccessDeniedException
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResult<String> handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor()
			.getMessage("AbstractAccessDecisionManager.accessDenied", e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg);
		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(SystemResultCode.FORBIDDEN, e.getLocalizedMessage());
	}

}
