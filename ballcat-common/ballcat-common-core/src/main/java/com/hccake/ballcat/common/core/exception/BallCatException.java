package com.hccake.ballcat.common.core.exception;

import com.hccake.ballcat.common.core.result.ResultMsg;
import lombok.Getter;

/**
 * 系统自定义异常
 * @author Hccake
 */
@Getter
public class BallCatException extends RuntimeException {

    private String msg;
    private int code;
    
    public BallCatException(ResultMsg resultMsg) {
		super(resultMsg.getMessage());
		this.code = resultMsg.getCode();
		this.msg = resultMsg.getMessage();
	}
	
	public BallCatException(ResultMsg resultMsg, Throwable e) {
		super(resultMsg.getMessage(), e);
		this.code = resultMsg.getCode();
		this.msg = resultMsg.getMessage();
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
