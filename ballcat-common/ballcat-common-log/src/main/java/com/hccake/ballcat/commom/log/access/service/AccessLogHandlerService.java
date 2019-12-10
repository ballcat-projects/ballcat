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
     * 记录日志
     * @param request
     * @param response
     * @param executionTime
     * @param throwable
     */
    default void logRecord(HttpServletRequest request, HttpServletResponse response, Long executionTime, Throwable throwable){
        T log = prodLog(request, response, executionTime, throwable);
        saveLog(log);
    }

    /**
     * 生产一个日志
     * @return accessLog
     * @param request
     * @param response
     * @param executionTime
     * @param throwable
     */
    T prodLog(HttpServletRequest request, HttpServletResponse response, Long executionTime, Throwable throwable);


    /**
     * 保存日志
     * 落库/或输出到文件等
     *
     * @param accessLog
     */
    void saveLog(T accessLog);

}
