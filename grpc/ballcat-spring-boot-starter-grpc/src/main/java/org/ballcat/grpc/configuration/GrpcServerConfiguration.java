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

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import org.ballcat.grpc.GrpcServer;
import org.ballcat.grpc.properties.GrpcServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-17 09:15
 */
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GrpcServer grpcServer(GrpcServerProperties properties, List<ServerInterceptor> interceptors,
			List<BindableService> services) {
		ServerBuilder<?> builder = ServerBuilder.forPort(properties.getPort())
			// 单个消息最大大小
			.maxInboundMessageSize((int) properties.getMessageSize().toBytes())
			.keepAliveTime(properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
			.keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.MILLISECONDS);

		// 按照spring生态的 @Order 排序
		interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
		// 获取一个游标在尾部的迭代器
		ListIterator<ServerInterceptor> iterator = interceptors.listIterator(interceptors.size());
		// 服务端是最后注册的拦截器最先执行, 所以要倒序注册
		while (iterator.hasPrevious()) {
			builder.intercept(iterator.previous());
		}

		// 注册服务
		for (BindableService service : services) {
			builder.addService(service);
		}

		return new GrpcServer(builder.build());
	}

}
