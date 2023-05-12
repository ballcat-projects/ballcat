package com.hccake.starter.grpc.constant;

import lombok.experimental.UtilityClass;
import org.springframework.core.Ordered;

/**
 * @author lingting 2023-04-17 10:24
 */
@UtilityClass
public class GrpcConstants {

	public static final int ORDER_TRACE_ID = Ordered.HIGHEST_PRECEDENCE;

	public static final int ORDER_CROSS = ORDER_TRACE_ID + 100;

	public static final int ORDER_LOG = Ordered.LOWEST_PRECEDENCE;

}
