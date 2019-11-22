package com.hccake.ballcat.commom.log.access.filter;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.commom.log.access.service.AccessLogHandlerService;
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
    private final AccessLogHandlerService accessLogService;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 排除监控请求 TODO 可配置
        if (StrUtil.containsAnyIgnoreCase(request.getRequestURI(),"/actuator")){
            filterChain.doFilter(request, response);
            return;
        }

        RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(request);

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
            Long time = endTime - startTime;

            // 记录在doFilter里被程序处理过后的异常，可参考 http://www.runoob.com/servlet/servlet-exception-handling.html
            Throwable throwable = (Throwable) requestWrapper.getAttribute("javax.servlet.error.exception");
            if (throwable != null) {
                myThrowable = throwable;
            }

            // 生产一个日志
            // 备注这里的request已经被xss过滤器包装过了 所以可以重复读取body
            Object object = accessLogService.prodLog(requestWrapper, response, time, myThrowable);
            // 日志记录
            accessLogService.saveLog(object);
        }

    }

}
