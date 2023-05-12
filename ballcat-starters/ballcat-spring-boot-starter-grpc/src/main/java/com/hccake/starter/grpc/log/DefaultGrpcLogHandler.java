package com.hccake.starter.grpc.log;

import com.hccake.starter.grpc.enums.GrpcLogType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-04-24 19:41
 */
@Slf4j
public class DefaultGrpcLogHandler implements GrpcLogHandler {

	@Override
	public void save(GrpcLog grpcLog) {
		if (GrpcLogType.SEND.equals(grpcLog.getType())) {
			log.debug("Grpc请求日志: 向{}:{} 的 {}/{} 发起请求, 耗时: {} 毫秒", grpcLog.getHost(), grpcLog.getPort(),
					grpcLog.getService(), grpcLog.getMethod(), grpcLog.getTime());
		}
		else {
			log.debug("Grpc请求日志: 收到{}:{} 对 {}/{} 的请求, 耗时: {} 毫秒", grpcLog.getHost(), grpcLog.getPort(),
					grpcLog.getService(), grpcLog.getMethod(), grpcLog.getTime());
		}
	}

}
