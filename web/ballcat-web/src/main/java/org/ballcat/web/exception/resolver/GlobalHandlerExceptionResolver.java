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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.constant.GlobalConstants;
import org.ballcat.common.core.exception.BusinessException;
import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.ballcat.common.model.result.R;
import org.ballcat.common.model.result.SystemResultCode;
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
	public R<String> handleGlobalException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 全局异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		this.globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = GlobalConstants.ENV_PROD.equals(this.profile) ? PROD_ERR_MSG : e.getLocalizedMessage();
		return R.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
		log.error("请求地址: {}, 空指针异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		this.globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = GlobalConstants.ENV_PROD.equals(this.profile) ? PROD_ERR_MSG : NLP_MSG;
		return R.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public R<String> handleMethodArgumentTypeMismatchException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求入参异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		this.globalExceptionHandler.handle(e);
		String errorMessage = GlobalConstants.ENV_PROD.equals(this.profile) ? PROD_ERR_MSG : e.getMessage();
		return R.failed(SystemResultCode.BAD_REQUEST, errorMessage);
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求方式异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		this.globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		log.error("请求地址: {}, 非法数据输入 ex={}", request.getRequestURI(), e.getMessage(), e);
		this.globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getMessage());
	}

	/**
	 * validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleBodyValidException(BindException e, HttpServletRequest request) {
		BindingResult bindingResult = e.getBindingResult();
		String errorMsg = bindingResult.getErrorCount() > 0 ? bindingResult.getAllErrors().get(0).getDefaultMessage()
				: "未获取到错误信息!";

		log.error("请求地址: {}, 参数绑定异常 ex={}", request.getRequestURI(), errorMsg);
		this.globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, errorMsg);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleValidationException(ValidationException e, HttpServletRequest request) {
		log.error("请求地址: {}, 参数校验异常 ex={}", request.getRequestURI(), e.getMessage());
		this.globalExceptionHandler.handle(e);
		return R.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * 自定义业务异常捕获 业务异常响应码推荐使用200 用 result 结构中的code做为业务错误码标识
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public R<String> handleBallCatException(BusinessException e, HttpServletRequest request) {
		log.error("请求地址: {}, 业务异常信息 ex={}", request.getRequestURI(), e.getMessage());
		this.globalExceptionHandler.handle(e);
		return R.failed(e.getCode(), e.getMessage());
	}

}
