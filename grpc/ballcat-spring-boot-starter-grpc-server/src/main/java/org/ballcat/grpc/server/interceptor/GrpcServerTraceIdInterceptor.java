/*
 * Copyright 2023-2024 the original author or authors.
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

package org.ballcat.grpc.server.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.ballcat.common.core.constant.MDCConstants;
import org.ballcat.grpc.server.properties.GrpcServerProperties;
import org.bson.types.ObjectId;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * 在服务器端，按照拦截器注册的顺序从后到前执行，先执行后面的拦截器，再执行前面的拦截器。
 *
 * @author lingting 2023-04-13 13:23
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GrpcServerTraceIdInterceptor implements ServerInterceptor {

	private final Metadata.Key<String> traceIdKey;

	public GrpcServerTraceIdInterceptor(GrpcServerProperties properties) {
		this.traceIdKey = Metadata.Key.of(properties.getTraceIdKey(), Metadata.ASCII_STRING_MARSHALLER);
	}

	/**
	 * 从请求中获取traceId, 如果没有返回生成的traceId
	 */
	protected String traceId(Metadata headers) {
		String traceId = null;
		if (headers.containsKey(this.traceIdKey)) {
			traceId = headers.get(this.traceIdKey);
		}
		if (StringUtils.hasText(traceId)) {
			return traceId;
		}
		return ObjectId.get().toString();
	}

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {
		String traceId = traceId(headers);
		MDC.put(MDCConstants.TRACE_ID_KEY, traceId);
		try {
			// 返回traceId
			headers.put(this.traceIdKey, traceId);
			return next.startCall(call, headers);
		}
		finally {
			MDC.remove(MDCConstants.TRACE_ID_KEY);
		}
	}

}
