package com.hccake.starter.grpc.log;

import com.hccake.starter.grpc.enums.GrpcLogType;
import lombok.Data;

/**
 * @author lingting 2023-04-13 14:36
 */
@Data
public class GrpcLog {

	private String traceId;

	private GrpcLogType type;

	/**
	 * 服务名
	 */
	private String service;

	/**
	 * 方法名
	 */
	private String method;

	/**
	 * host
	 */
	private String host;

	/**
	 * port
	 */
	private String port;

	/**
	 * 耗时, 单位: 毫秒
	 */
	private Long time;

}
