package com.hccake.starter.grpc.properties;

import lombok.Data;
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

	private DataSize messageSize = DataSize.ofMegabytes(512);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.SECONDS.toMillis(10);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.MINUTES.toMillis(1);

}
