package com.hccake.starter.grpc;

import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.AbstractBlockingStub;
import io.grpc.stub.AbstractFutureStub;
import com.hccake.starter.grpc.properties.GrpcClientProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author lingting 2023-04-06 15:33
 */
@Getter
@RequiredArgsConstructor
public class GrpcClient<S extends AbstractAsyncStub<S>, B extends AbstractBlockingStub<B>, F extends AbstractFutureStub<F>> {

	private final GrpcClientProperties properties;

	private final ManagedChannel channel;

	private final S asyncStub;

	private final B blockingStub;

	private final F futureStub;

	public <T> T async(Function<S, T> function) {
		return function.apply(asyncStub);
	}

	public <T> T blocking(Function<B, T> function) {
		return function.apply(blockingStub);
	}

	public <T> T future(Function<F, T> function) {
		return function.apply(futureStub);
	}

	public void async(Consumer<S> consumer) {
		consumer.accept(asyncStub);
	}

	public void blocking(Consumer<B> consumer) {
		consumer.accept(blockingStub);
	}

	public void future(Consumer<F> consumer) {
		consumer.accept(futureStub);
	}

}
