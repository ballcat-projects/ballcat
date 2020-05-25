package com.hccake.ballcat.common.conf.exception;

import com.hccake.ballcat.common.conf.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.core.result.R;
import com.hccake.ballcat.common.core.result.SystemResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.List;

/**
 * 全局异常处理
 * @author Hccake
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerResolver {
	private final GlobalExceptionHandler globalExceptionHandler;

	/**
	 * 全局异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.SERVER_ERROR, e.getLocalizedMessage());
	}


	/**
	 * 自定义业务异常捕获
	 * 业务异常响应码推荐使用200
	 * 用 result 结构中的code做为业务错误码标识
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public R handleBallCatException(BusinessException e) {
		log.error("自定义异常信息 ex={}", e.getMessage(), e);
		globalExceptionHandler.handle(e);
		return R.failed(e.getCode(), e.getMsg());
	}


	/**
	 * AccessDeniedException
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor()
				.getMessage("AbstractAccessDecisionManager.accessDenied"
						, e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg, e);
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.FORBIDDEN, e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 *
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(Exception exception) {
		List<FieldError> fieldErrors;
		if(exception instanceof BindException){
			BindException bindException = (BindException)exception;
			fieldErrors = bindException.getBindingResult().getFieldErrors();
		}else{
			MethodArgumentNotValidException e = (MethodArgumentNotValidException)exception;
			fieldErrors = e.getBindingResult().getFieldErrors();
		}

		String errorMsg = fieldErrors.get(0).getDefaultMessage();

		log.error("参数绑定异常,ex = {}", errorMsg);
		globalExceptionHandler.handle(exception);
		return R.failed(SystemResultCode.BAD_REQUEST, errorMsg);
	}


	/**
	 * 单体参数校验异常
	 * validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleValidationException(Exception e) {
		log.error("参数绑定异常 ex={}", e.getMessage(), e);
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}



}
