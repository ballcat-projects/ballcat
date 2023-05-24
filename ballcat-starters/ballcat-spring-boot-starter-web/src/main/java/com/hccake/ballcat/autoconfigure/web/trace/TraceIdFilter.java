package com.hccake.ballcat.autoconfigure.web.trace;

import com.hccake.ballcat.common.core.constant.MDCConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TraceId 过滤器
 * <p>
 * 利用 Slf4J 的 MDC 功能，将 traceId 放入 MDC 中，方便日志打印
 *
 * @author Hccake 2020/5/25 17:35
 */
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

	private final String traceIdHeaderName;

	private final TraceIdGenerator traceIdGenerator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 先获取请求头中的 traceId，如果没有，则生成一个
		String traceId = request.getHeader(traceIdHeaderName);
		if (traceId == null || traceId.isEmpty()) {
			traceId = traceIdGenerator.generate();
		}

		MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
		try {
			// 响应头中添加 traceId 参数，方便排查问题
			response.setHeader(traceIdHeaderName, traceId);
			filterChain.doFilter(request, response);
		}
		finally {
			MDC.remove(MDCConstants.TRACE_ID_KEY);
		}
	}

}
