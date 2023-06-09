package org.ballcat.security.web;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.model.result.R;
import org.ballcat.common.model.result.SystemResultCode;
import org.ballcat.security.exception.AuthorizationException;
import org.ballcat.security.exception.PermissionsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionHandler {

	/**
	 * 鉴权异常
	 */
	@ExceptionHandler(AuthorizationException.class)
	public R<String> handlerAuthorizationException(AuthorizationException e) {
		log.error("鉴权异常! {}", e.getMessage());
		return R.failed(SystemResultCode.UNAUTHORIZED);
	}

	/**
	 * 权限异常
	 */
	@ExceptionHandler(PermissionsException.class)
	public R<String> handlerPermissionsException(PermissionsException e) {
		log.error("权限异常! {}", e.getMessage());
		return R.failed(SystemResultCode.FORBIDDEN);
	}

}
