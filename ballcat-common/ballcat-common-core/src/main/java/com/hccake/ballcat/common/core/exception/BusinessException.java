package com.hccake.ballcat.common.core.exception;

import com.hccake.ballcat.common.core.result.ResultCode;
import lombok.Getter;

/**
 * 通用业务异常
 *
 * @author Hccake
 */
@Getter
public class BusinessException extends RuntimeException {

	private final String message;

	private final int code;

	public BusinessException(ResultCode resultCode) {
		super(resultCode.getMessage());
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	public BusinessException(ResultCode resultCode, Throwable e) {
		super(resultCode.getMessage(), e);
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	public BusinessException(int code, String message) {
		super(message);
		this.message = message;
		this.code = code;
	}

	public BusinessException(int code, String message, Throwable e) {
		super(message, e);
		this.message = message;
		this.code = code;
	}

}
