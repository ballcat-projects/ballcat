package com.hccake.starter.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.AbstractBlockingStub;
import io.grpc.stub.AbstractFutureStub;
import com.hccake.starter.grpc.properties.GrpcClientProperties;
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
