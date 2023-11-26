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
package org.ballcat.grpc.configuration;

import org.ballcat.grpc.GrpcClientChannel;
import org.ballcat.grpc.interceptor.TraceIdInterceptor;
import org.ballcat.grpc.properties.GrpcClientProperties;
import org.ballcat.grpc.properties.GrpcProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-06 15:21
 */
@AutoConfiguration
@EnableConfigurationProperties({ GrpcClientProperties.class, GrpcProperties.class })
public class GrpcClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = GrpcClientProperties.PREFIX, name = "host")
	public GrpcClientChannel grpcChannel(GrpcClientProperties properties) {
		if (properties.getPort() == null || properties.getPort() < 0) {
			throw new IllegalArgumentException(GrpcClientProperties.PREFIX + ".port值不能为: " + properties.getPort());
		}
		return new GrpcClientChannel(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public TraceIdInterceptor traceIdInterceptor(GrpcProperties properties) {
		return new TraceIdInterceptor(properties.getTraceId());
	}

}
