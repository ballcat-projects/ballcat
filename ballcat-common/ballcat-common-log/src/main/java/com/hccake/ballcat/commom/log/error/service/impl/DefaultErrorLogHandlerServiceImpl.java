package com.hccake.ballcat.commom.log.error.service.impl;

import com.hccake.ballcat.commom.log.error.service.ErrorLogHandlerService;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/18 17:06
 * 默认的异常日志处理类
 */
public class DefaultErrorLogHandlerServiceImpl implements ErrorLogHandlerService {
    /**
     * 在此处理日志
     * 默认什么都不处理
     * @param throwable
     */
    @Override
    public void handle(Throwable throwable) {}
}
