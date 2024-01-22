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

package org.ballcat.grpc.server.configuration;

import java.util.List;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import lombok.RequiredArgsConstructor;
import org.ballcat.grpc.server.GrpcServer;
import org.ballcat.grpc.server.interceptor.GrpcServerTraceIdInterceptor;
import org.ballcat.grpc.server.properties.GrpcServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-17 09:15
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerConfiguration {

	private final GrpcServerProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public GrpcServer grpcServer(List<ServerInterceptor> interceptors, List<BindableService> services) {
		return new GrpcServer(this.properties, interceptors, services);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerTraceIdInterceptor grpcServerTraceIdInterceptor() {
		return new GrpcServerTraceIdInterceptor(this.properties);
	}

}
