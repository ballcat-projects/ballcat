package com.hccake.ballcat.common.core.exception.handler;

/**
 * 异常日志处理类
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/18 17:05
 */
public interface GlobalExceptionHandler {

	/**
	 * 在此处理错误信息 进行落库，入ES， 发送报警通知等信息
	 * @param throwable 异常
	 */
	void handle(Throwable throwable);

}
