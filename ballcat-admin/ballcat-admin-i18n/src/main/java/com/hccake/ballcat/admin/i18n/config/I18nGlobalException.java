package com.hccake.ballcat.admin.i18n.config;

import com.hccake.ballcat.common.conf.exception.GlobalExceptionHandlerResolver;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.common.i18n.execute.TranslateExecuteWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 国际化全局异常处理器
 *
 * @author Yakir
 */
@Slf4j
@RestControllerAdvice
public class I18nGlobalException extends GlobalExceptionHandlerResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	public I18nGlobalException(GlobalExceptionHandler globalExceptionHandler) {
		super(globalExceptionHandler);
		this.globalExceptionHandler = globalExceptionHandler;

	}

	@Value("${spring.profiles.active:prod}")
	private String profile;

	/**
	 * 异常业务key
	 */
	@Value("${ballcat.i18n.exceptionKey:exception}")
	private String exceptionCode;

	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	public static final String NLP_MSG = "空指针异常!";

	/**
	 * 全局异常捕获
	 * @param e the e
	 * @return R
	 */
	@Override
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMsg = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG
				: (e instanceof NullPointerException ? NLP_MSG : e.getLocalizedMessage());
		String exceptionMsg = executeTranslate("global.exception", SystemResultCode.BAD_REQUEST.getCode(), errorMsg);
		return R.failed(SystemResultCode.SERVER_ERROR, exceptionMsg);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@Override
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleMethodArgumentTypeMismatchException(Exception e) {
		log.error("请求入参异常 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		String message = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : e.getMessage();

		String exceptionMsg = executeTranslate("method.argument.type.exception", SystemResultCode.BAD_REQUEST.getCode(),
				message);

		return R.failed(SystemResultCode.BAD_REQUEST, exceptionMsg);
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@Override
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(Exception e) {
		log.error("请求方式异常 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		String exceptionMsg = executeTranslate("request.not.support.exception", SystemResultCode.BAD_REQUEST.getCode(),
				e.getLocalizedMessage());
		return R.failed(SystemResultCode.BAD_REQUEST, exceptionMsg);
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@Override
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("非法数据输入 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		String exceptionMsg = executeTranslate("illegal.argument.exception", SystemResultCode.BAD_REQUEST.getCode(),
				e.getMessage());
		return R.failed(SystemResultCode.BAD_REQUEST, exceptionMsg);
	}

	/**
	 * validation Exception
	 * @param exception e
	 * @return R
	 */
	@Override
	@ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleBodyValidException(Exception exception) {
		BindingResult bindingResult;

		if (exception instanceof BindException) {
			bindingResult = ((BindException) exception).getBindingResult();
		}
		else {
			bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
		}

		String errorMsg = bindingResult.getErrorCount() > 0 ? bindingResult.getAllErrors().get(0).getDefaultMessage()
				: "未获取到错误信息!";

		log.error("参数绑定异常,ex = {}", errorMsg);
		globalExceptionHandler.handle(exception);
		String exceptionMsg = executeTranslate("body.valid.exception", SystemResultCode.BAD_REQUEST.getCode(),
				errorMsg);
		return R.failed(SystemResultCode.BAD_REQUEST, exceptionMsg);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@Override
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleValidationException(Exception e) {
		log.error("参数绑定异常 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		String exceptionMsg = executeTranslate("valid.exception", SystemResultCode.BAD_REQUEST.getCode(),
				e.getLocalizedMessage());
		return R.failed(SystemResultCode.BAD_REQUEST, exceptionMsg);
	}

	/**
	 * AccessDeniedException
	 * @param e the e
	 * @return R
	 */
	@Override
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R<String> handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor().getMessage("AbstractAccessDecisionManager.accessDenied",
				e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg);
		globalExceptionHandler.handle(e);
		String exceptionMsg = executeTranslate("access.denied.exception", SystemResultCode.FORBIDDEN.getCode(),
				e.getLocalizedMessage());
		return R.failed(SystemResultCode.FORBIDDEN, exceptionMsg);
	}

	/**
	 * 自定义业务异常捕获 业务异常响应码推荐使用200 用 result 结构中的code做为业务错误码标识
	 * @param e the e
	 * @return R
	 */
	@Override
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public R<String> handleBallCatException(BusinessException e) {
		log.error("业务异常信息 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		String exceptionMsg = executeTranslate("ballcat.exception", e.getCode(), e.getMessage());
		return R.failed(e.getCode(), exceptionMsg);
	}

	private String executeTranslate(String code, Integer statusCode, String exceptionMsg) {
		return TranslateExecuteWrapper.translateText(exceptionCode, code, prodI18nErrorParam(statusCode, exceptionMsg));
	}

	private Map<String, String> prodI18nErrorParam(Integer code, String exceptionMsg) {
		Map<String, String> params = new HashMap<>();
		params.put("code", String.valueOf(code));
		params.put("msg", exceptionMsg);
		return params;
	}

}
