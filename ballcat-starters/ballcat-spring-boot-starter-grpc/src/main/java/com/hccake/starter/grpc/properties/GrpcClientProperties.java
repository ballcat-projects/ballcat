package com.hccake.starter.grpc.properties;

import lombok.Data;
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

	private boolean usePlaintext = true;

	private boolean enableRetry = true;

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.SECONDS.toMillis(10);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.MINUTES.toMillis(1);

}
