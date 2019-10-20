package com.hccake.ballcat.commom.log.access.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 22:21
 */
public interface AccessLogHandlerService<T> {

    /**
     * 生产一个日志
     * @return accessLog
     * @param request
     * @param response
     * @param time
     * @param myThrowable
     */
    T prodLog(HttpServletRequest request, HttpServletResponse response, Long time, Throwable myThrowable);


    /**
     * 记录日志
     *
     * @param accessLog
     */
    void saveLog(T accessLog);



}
