package com.hccake.ballcat.common.core.exception;

import com.hccake.ballcat.common.core.result.ResultCode;
import lombok.Getter;

/**
 * 通用业务异常
 * @author Hccake
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String msg;
    private final int code;
    
    public BusinessException(ResultCode resultCode) {
		super(resultCode.getMessage());
		this.code = resultCode.getCode();
		this.msg = resultCode.getMessage();
	}
	
	public BusinessException(ResultCode resultCode, Throwable e) {
		super(resultCode.getMessage(), e);
		this.code = resultCode.getCode();
		this.msg = resultCode.getMessage();
	}
	
	public BusinessException(int code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public BusinessException(int code, String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
}
