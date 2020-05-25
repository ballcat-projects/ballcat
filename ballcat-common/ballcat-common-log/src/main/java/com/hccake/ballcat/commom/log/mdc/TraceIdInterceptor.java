package com.hccake.ballcat.commom.log.mdc;

import cn.hutool.core.util.IdUtil;
import com.hccake.ballcat.commom.log.constant.LogConstant;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 17:35
 * 利用 Slf4J 的 MDC 功能，记录 TraceId
 */
public class TraceIdInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(LogConstant.TRACE_ID, IdUtil.objectId());
        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        MDC.remove(LogConstant.TRACE_ID);
        super.afterCompletion(request, response, handler, ex);
    }

}
