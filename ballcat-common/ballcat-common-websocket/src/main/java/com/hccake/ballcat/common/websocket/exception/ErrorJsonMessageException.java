package com.hccake.ballcat.common.websocket.exception;

/**
 * 错误的 json 消息
 *
 * @author hccake
 */
public class ErrorJsonMessageException extends RuntimeException {

	public ErrorJsonMessageException(String message) {
		super(message);
	}

}
