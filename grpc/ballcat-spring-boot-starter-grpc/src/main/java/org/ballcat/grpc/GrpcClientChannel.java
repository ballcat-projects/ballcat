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
package org.ballcat.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.AbstractBlockingStub;
import io.grpc.stub.AbstractFutureStub;
import org.ballcat.grpc.properties.GrpcClientProperties;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author lingting 2023-04-06 15:17
 */
public class GrpcClientChannel implements DisposableBean {

	private final GrpcClientProperties properties;

	private final ManagedChannel channel;

	public GrpcClientChannel(GrpcClientProperties properties) {
		this(properties, builder -> builder);
	}

	public GrpcClientChannel(GrpcClientProperties properties, UnaryOperator<ManagedChannelBuilder<?>> operator) {
		ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
			.forAddress(properties.getHost(), properties.getPort())
			.keepAliveTime(properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
			.keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.MILLISECONDS);

		if (properties.isUsePlaintext()) {
			channelBuilder.usePlaintext();
		}
		// 重试
		if (properties.isEnableRetry()) {
			channelBuilder.enableRetry();
		}

		this.properties = properties;
		this.channel = operator.apply(channelBuilder).build();
	}

	public <S extends AbstractAsyncStub<S>, B extends AbstractBlockingStub<B>, F extends AbstractFutureStub<F>> GrpcClient<S, B, F> client(
			Function<Channel, S> asyncFunction, Function<Channel, B> blockingFunction,
			Function<Channel, F> futureFunction) {
		return new GrpcClient<>(properties, channel, asyncFunction.apply(channel), blockingFunction.apply(channel),
				futureFunction.apply(channel));
	}

	@Override
	public void destroy() {
		close();
	}

	public void close() {
		channel.shutdown();
	}

}
