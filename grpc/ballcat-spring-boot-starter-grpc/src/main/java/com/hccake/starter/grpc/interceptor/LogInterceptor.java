/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hccake.starter.grpc.interceptor;

import cn.hutool.core.convert.Convert;
import com.hccake.ballcat.common.system.StopWatch;
import io.grpc.*;
import com.hccake.starter.grpc.enums.GrpcLogType;
import com.hccake.starter.grpc.log.GrpcLog;
import com.hccake.starter.grpc.log.GrpcLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Supplier;

/**
 * 服务端注册要确保在traceId后面
 *
 * @author lingting 2023-04-13 13:29
 */
@Slf4j
@Order
@RequiredArgsConstructor
public class LogInterceptor implements ServerInterceptor, ClientInterceptor {

	private final GrpcLogHandler handler;

	@Override
	public <S, R> ClientCall<S, R> interceptCall(MethodDescriptor<S, R> method, CallOptions callOptions, Channel next) {
		GrpcLog grpcLog = new GrpcLog();
		grpcLog.setType(GrpcLogType.SEND);
		grpcLog.setMethod(method.getBareMethodName());
		grpcLog.setService(method.getServiceName());

		try {
			return timing(() -> next.newCall(method, callOptions), grpcLog);
		}
		finally {
			resolve(next.authority(), grpcLog);
			handler.save(grpcLog);
		}
	}

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {

		GrpcLog grpcLog = new GrpcLog();
		grpcLog.setType(GrpcLogType.SEND);
		MethodDescriptor<S, R> method = call.getMethodDescriptor();
		grpcLog.setMethod(method.getBareMethodName());
		grpcLog.setService(method.getServiceName());

		try {
			return timing(() -> next.startCall(call, headers), grpcLog);
		}
		finally {
			resolve(call, grpcLog);
			handler.save(grpcLog);
		}
	}

	<T> T timing(Supplier<T> supplier, GrpcLog grpcLog) {
		StopWatch watch = new StopWatch();
		try {
			watch.start();
			return supplier.get();
		}
		finally {
			watch.stop();
			grpcLog.setTime(watch.timeMillis());
		}
	}

	<S, R> void resolve(ServerCall<S, R> call, GrpcLog grpcLog) {
		Attributes attributes = call.getAttributes();
		SocketAddress address = attributes.get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
		if (address instanceof InetSocketAddress) {
			InetSocketAddress socketAddress = (InetSocketAddress) address;
			InetAddress inetAddress = socketAddress.getAddress();
			grpcLog.setHost(inetAddress.getHostAddress());
			grpcLog.setPort(Convert.toStr(socketAddress.getPort()));
		}
	}

	void resolve(String authority, GrpcLog grpcLog) {
		String[] split = authority.split(":");
		grpcLog.setHost(split[0]);
		if (split.length > 1) {
			grpcLog.setPort(split[1]);
		}
	}

}
