package com.hccake.ballcat.common.log.mdc;

import cn.hutool.core.util.IdUtil;
import com.hccake.ballcat.common.log.constant.LogConstant;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 17:35 利用 Slf4J 的 MDC 功能，记录 TraceId
 */
public class TraceIdFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String traceId = IdUtil.objectId();
		MDC.put(LogConstant.TRACE_ID, traceId);
		try {
			// 响应头中添加 traceId 参数，方便排查问题
			response.setHeader(LogConstant.TRACE_ID, traceId);
			filterChain.doFilter(request, response);
		}
		finally {
			MDC.remove(LogConstant.TRACE_ID);
		}
	}

}
