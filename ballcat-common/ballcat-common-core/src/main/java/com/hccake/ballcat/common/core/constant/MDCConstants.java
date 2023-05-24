package com.hccake.ballcat.common.core.constant;

/**
 * MDC 相关常量
 *
 * @author hccake
 * @since 1.3.1
 */
public final class MDCConstants {

	private MDCConstants() {
	}

	/**
	 * 跟踪ID，用于一次请求或执行方法时，产生的各种日志间的数据关联
	 */
	public static final String TRACE_ID_KEY = "traceId";

}
