package com.hccake.ballcat.autoconfigure.web.trace;

import javax.servlet.http.HttpServletRequest;

/**
 * TraceId 转换器
 *
 * @author hccake
 * @since 1.3.1
 */
@FunctionalInterface
public interface TraceIdGenerator {

	/**
	 * 生成 traceId
	 * @return traceId
	 */
	String generate();

}
