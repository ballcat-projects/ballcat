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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.exception.BusinessException;
import org.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.ballcat.common.model.result.ApiResult;
import org.ballcat.common.model.result.SystemResultCode;
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
@Setter
@Order
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerExceptionResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	/**
	 * 隐藏异常的详细信息。
	 */
	private Boolean hideExceptionDetails = true;

	/**
	 * 设置隐藏后的提示信息。
	 */
	private String hiddenMessage = "系统异常，请联系管理员";

	private String npeErrorMessage = "空指针异常!";

	private boolean isHideExceptionDetails() {
		return Boolean.TRUE.equals(this.hideExceptionDetails);
	}

	/**
	 * 全局异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResult<String> handleGlobalException(Exception e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 全局异常信息 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = isHideExceptionDetails() ? this.hiddenMessage : e.getLocalizedMessage();
		return ApiResult.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResult<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 空指针异常 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = isHideExceptionDetails() ? this.hiddenMessage : this.npeErrorMessage;
		return ApiResult.failed(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ApiResult<String> handleMethodArgumentTypeMismatchException(Exception e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 请求入参异常 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public ApiResult<String> requestNotSupportedException(Exception e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 请求方式异常 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResult<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 非法数据输入 ex=%s", request.getRequestURI(), e.getMessage()), e);
		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResult<String> handleBodyValidException(BindException e, HttpServletRequest request) {
		BindingResult bindingResult = e.getBindingResult();
		String errorMsg = bindingResult.getErrorCount() > 0 ? bindingResult.getAllErrors().get(0).getDefaultMessage()
				: "未获取到错误信息!";

		log.error(String.format("请求地址: %s, 非法数据输入 ex=%s", request.getRequestURI(), errorMsg));

		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(SystemResultCode.BAD_REQUEST, errorMsg);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResult<String> handleValidationException(ValidationException e, HttpServletRequest request) {
		log.error(String.format("请求地址: %s, 参数校验异常 ex=%s", request.getRequestURI(), e.getMessage()));
		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * 自定义业务异常捕获 业务异常响应码推荐使用200 用 result 结构中的code做为业务错误码标识
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<String> handleBallCatException(BusinessException e, HttpServletRequest request) {
		log.error("请求地址: {}, 业务异常信息 ex={}", request.getRequestURI(), e.getMessage());
		this.globalExceptionHandler.handle(e);
		return ApiResult.failed(e.getCode(), e.getMessage());
	}

}
