package com.hccake.ballcat.common.conf.exception.handler;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/18 17:06
 * 默认的异常日志处理类
 */
public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {
    /**
     * 在此处理日志
     * 默认什么都不处理
     * @param throwable 异常信息
     */
    @Override
    public void handle(Throwable throwable) {}
}
