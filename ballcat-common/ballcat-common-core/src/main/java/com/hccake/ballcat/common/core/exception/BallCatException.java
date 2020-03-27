package com.hccake.ballcat.common.core.exception;

import com.hccake.ballcat.common.core.result.ResultCode;
import lombok.Getter;

/**
 * 通用业务异常
 * @author Hccake
 */
@Getter
public class BallCatException extends RuntimeException {

    private String msg;
    private int code;
    
    public BallCatException(ResultCode resultCode) {
		super(resultCode.getMessage());
		this.code = resultCode.getCode();
		this.msg = resultCode.getMessage();
	}
	
	public BallCatException(ResultCode resultCode, Throwable e) {
		super(resultCode.getMessage(), e);
		this.code = resultCode.getCode();
		this.msg = resultCode.getMessage();
	}
	
	public BallCatException(int code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public BallCatException( int code, String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
}
