package com.hccake.ballcat.autoconfigure.web.exception.resolver;

import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	public R<String> handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor().getMessage("AbstractAccessDecisionManager.accessDenied",
				e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg);
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.FORBIDDEN, e.getLocalizedMessage());
	}

}
