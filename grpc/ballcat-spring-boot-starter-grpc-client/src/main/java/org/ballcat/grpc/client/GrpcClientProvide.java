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

package org.ballcat.grpc.client;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.AbstractBlockingStub;
import io.grpc.stub.AbstractFutureStub;
import io.grpc.stub.AbstractStub;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ballcat.grpc.client.properties.GrpcClientProperties;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;

/**
 * @author lingting 2023-12-15 14:44
 */
@RequiredArgsConstructor
public class GrpcClientProvide {

	protected final GrpcClientProperties properties;

	protected final List<ClientInterceptor> interceptors;

	public ManagedChannel channel() {
		return channel(this.properties.getHost(), this.properties.getPort());
	}

	public ManagedChannel channel(String host, Integer port) {
		return channel(host, port, builder -> builder);
	}

	public ManagedChannel channel(String host, Integer port, UnaryOperator<ManagedChannelBuilder<?>> operator) {
		if (port == null || port < 0) {
			throw new IllegalArgumentException(GrpcClientProperties.PREFIX + ".port值不能为: " + port);
		}
		return channel(String.format("%s:%d", host, port), operator);
	}

	public ManagedChannel channel(String target, UnaryOperator<ManagedChannelBuilder<?>> operator) {
		ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forTarget(target);
		ManagedChannelBuilder<?> apply = operator.apply(builder);
		return channel(apply);
	}

	public ManagedChannel channel(ManagedChannelBuilder<?> builder) {
		// 开启心跳
		if (this.properties.isEnableKeepAlive()) {
			builder.keepAliveTime(this.properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
				.keepAliveTimeout(this.properties.getKeepAliveTimeout(), TimeUnit.MILLISECONDS);
		}

		// 使用明文
		if (this.properties.isUsePlaintext()) {
			builder.usePlaintext();
		}

		// 重试
		if (this.properties.isEnableRetry()) {
			builder.enableRetry();
		}

		// 注册拦截器
		if (!CollectionUtils.isEmpty(this.interceptors)) {
			// 按照spring生态的 @Order 排序
			this.interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
			builder.intercept(this.interceptors);
		}

		// ssl配置
		buildSsl(builder);

		return builder.build();
	}

	public <R extends AbstractStub<R>> R stub(Channel channel, Function<Channel, R> function) {
		return function.apply(channel);
	}

	public <R extends AbstractAsyncStub<R>> R async(Channel channel, Function<Channel, R> function) {
		return stub(channel, function);
	}

	public <R extends AbstractBlockingStub<R>> R blocking(Channel channel, Function<Channel, R> function) {
		return stub(channel, function);
	}

	public <R extends AbstractFutureStub<R>> R future(Channel channel, Function<Channel, R> function) {
		return stub(channel, function);
	}

	@SneakyThrows
	@SuppressWarnings("java:S1066")
	protected void buildSsl(ManagedChannelBuilder<?> builder) {
		// 关闭ssl
		if (!this.properties.isUsePlaintext() && this.properties.isDisableSsl()) {
			if (builder instanceof NettyChannelBuilder) {
				SslContextBuilder sslContextBuilder = GrpcSslContexts.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE);

				((NettyChannelBuilder) builder).sslContext(sslContextBuilder.build());
			}
		}
	}

}
