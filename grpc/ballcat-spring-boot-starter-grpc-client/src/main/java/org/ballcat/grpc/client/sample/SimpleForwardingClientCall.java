package org.ballcat.grpc.client.sample;

import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;

/**
 * @author lingting 2023-12-18 19:13
 */
@SuppressWarnings("java:S2176")
public class SimpleForwardingClientCall<S, R> extends ForwardingClientCall.SimpleForwardingClientCall<S, R> {

	protected SimpleForwardingClientCall(ClientCall<S, R> delegate) {
		super(delegate);
	}

	@Override
	public void start(Listener<R> responseListener, Metadata headers) {
		onStartBefore(responseListener, headers);
		super.start(responseListener, headers);
		onStartAfter(responseListener, headers);
	}

	@Override
	public void sendMessage(S message) {
		onSendMessageBefore(message);
		super.sendMessage(message);
		onSendMessageAfter(message);
	}

	@Override
	public void halfClose() {
		onHalfCloseBefore();
		super.halfClose();
		onHalfCloseAfter();
	}

	public void onStartBefore(Listener<R> responseListener, Metadata headers) {
		//
	}

	public void onStartAfter(Listener<R> responseListener, Metadata headers) {
		//
	}

	public void onSendMessageBefore(S message) {
		//
	}

	public void onSendMessageAfter(S message) {
		//
	}

	public void onHalfCloseBefore() {
		//
	}

	public void onHalfCloseAfter() {
		//
	}

}
