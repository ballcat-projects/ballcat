package org.ballcat.grpc.server.properties;

import lombok.Data;
import org.ballcat.common.core.constant.MDCConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-06 15:18
 */
@Data
@ConfigurationProperties(GrpcServerProperties.PREFIX)
public class GrpcServerProperties {

	public static final String PREFIX = "ballcat.grpc.server";

	private Integer port;

	private String traceIdKey = MDCConstants.TRACE_ID_KEY;

	private DataSize messageSize = DataSize.ofMegabytes(512);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.HOURS.toMillis(2);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.SECONDS.toMillis(20);

}
