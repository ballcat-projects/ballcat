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
package org.ballcat.grpc.interceptor;

import io.grpc.*;
import org.ballcat.grpc.constant.GrpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

/**
 * @author lingting 2023-04-17 10:21
 */
@Slf4j
@Order(GrpcConstants.ORDER_CROSS)
public class CrossInterceptor implements ServerInterceptor {

	private static final Metadata.Key<String> KEY_ORIGIN = Metadata.Key.of("Origin", Metadata.ASCII_STRING_MARSHALLER);

	@Override
	public <R, S> ServerCall.Listener<R> interceptCall(ServerCall<R, S> call, Metadata headers,
			ServerCallHandler<R, S> next) {
		String origin = headers.get(KEY_ORIGIN);
		log.debug("收到来源: {} 的跨域请求.", origin);
		// 处理跨域请求
		return Contexts.interceptCall(Context.current(), call, headers, next);
	}

}
