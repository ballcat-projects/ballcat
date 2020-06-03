package com.hccake.ballcat.commom.log.access.filter;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.commom.log.access.handler.AccessLogHandler;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.core.filter.RepeatBodyRequestWrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 21:53
 */
@AllArgsConstructor
public class AccessLogFilter extends OncePerRequestFilter {
    private final AccessLogHandler<?> accessLogService;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request 请求信息
     * @param response 请求体
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 排除监控请求 TODO 可配置
        if (StrUtil.containsAnyIgnoreCase(request.getRequestURI(), "/actuator", "/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }


        // 包装request，以保证可以重复读取body 但不对文件上传请求body进行处理
        HttpServletRequest requestWrapper;
        if (LogUtils.isMultipartContent(request)) {
            requestWrapper = request;
        }else {
            requestWrapper = new RepeatBodyRequestWrapper(request);
        }


        // 开始时间
        Long startTime = System.currentTimeMillis();
        Throwable myThrowable = null;
        try {
            filterChain.doFilter(requestWrapper, response);
        } catch (Throwable throwable) {
            // 记录外抛异常
            myThrowable = throwable;
            throw throwable;
        } finally {
            // 结束时间
            Long endTime = System.currentTimeMillis();
            // 执行时长
            Long executionTime = endTime - startTime;
            // 记录在doFilter里被程序处理过后的异常，可参考 http://www.runoob.com/servlet/servlet-exception-handling.html
            Throwable throwable = (Throwable) requestWrapper.getAttribute("javax.servlet.error.exception");
            if (throwable != null) {
                myThrowable = throwable;
            }
            // 生产一个日志并记录
            accessLogService.logRecord(requestWrapper, response, executionTime, myThrowable);
        }

    }




}
