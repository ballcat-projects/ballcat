package org.ballcat.grpc.client.properties;

import lombok.Data;
import org.ballcat.common.core.constant.MDCConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-06 15:18
 */
@Data
@ConfigurationProperties(GrpcClientProperties.PREFIX)
public class GrpcClientProperties {

	public static final String PREFIX = "ballcat.grpc.client";

	private String host;

	private Integer port = 80;

	private String traceIdKey = MDCConstants.TRACE_ID_KEY;

	private boolean usePlaintext = false;

	/**
	 * 仅在支持此操作时生效
	 */
	private boolean disableSsl = false;

	private boolean enableRetry = true;

	private boolean enableKeepAlive = true;

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.MINUTES.toMillis(30);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.SECONDS.toMillis(2);

}
