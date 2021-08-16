package com.hccake.ballcat.autoconfigure.web.exception.resolver;

import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ValidationException;

/**
 * 全局异常处理
 *
 * @author Hccake
 */
@Order
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerExceptionResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	@Value("${spring.profiles.active:prod}")
	private String profile;

	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	public static final String NLP_MSG = "空指针异常!";

	/**
	 * 全局异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : e.getLocalizedMessage();
		return R.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleNullPointerException(NullPointerException e) {
		log.error("空指针异常 ex={}", e.getMessage(), e);
		globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : NLP_MSG;
		return R.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleMethodArgumentTypeMismatchException(Exception e) {
		log.error("请求入参异常 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);

		String errorMessage = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : e.getMessage();
		return R.failed(SystemResultCode.BAD_REQUEST, errorMessage);
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(Exception e) {
		log.error("请求方式异常 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("非法数据输入 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getMessage());
	}

	/**
	 * validation Exception
	 * @param exception e
	 * @return R
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleBodyValidException(BindException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		String errorMsg = bindingResult.getErrorCount() > 0 ? bindingResult.getAllErrors().get(0).getDefaultMessage()
				: "未获取到错误信息!";

		log.error("参数绑定异常,ex = {}", errorMsg);
		globalExceptionHandler.handle(exception);
		return R.failed(SystemResultCode.BAD_REQUEST, errorMsg);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleValidationException(ValidationException e) {
		log.error("参数校验异常 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * 自定义业务异常捕获 业务异常响应码推荐使用200 用 result 结构中的code做为业务错误码标识
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public R<String> handleBallCatException(BusinessException e) {
		log.error("业务异常信息 ex={}", e.getMessage());
		globalExceptionHandler.handle(e);
		return R.failed(e.getCode(), e.getMessage());
	}

}
