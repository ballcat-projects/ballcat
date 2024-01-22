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

package org.ballcat.grpc.client.configuration;

import java.util.List;

import io.grpc.ClientInterceptor;
import lombok.RequiredArgsConstructor;
import org.ballcat.grpc.client.GrpcClientProvide;
import org.ballcat.grpc.client.interceptor.GrpcClientTraceIdInterceptor;
import org.ballcat.grpc.client.properties.GrpcClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-06 15:21
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcClientProperties.class)
public class GrpcClientAutoConfiguration {

	private final GrpcClientProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientProvide grpcClientProvide(List<ClientInterceptor> interceptors) {
		return new GrpcClientProvide(this.properties, interceptors);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientTraceIdInterceptor grpcClientTraceIdInterceptor() {
		return new GrpcClientTraceIdInterceptor(this.properties);
	}

}
