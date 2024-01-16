package org.ballcat.grpc.server.sample;

import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;

/**
 * @author lingting 2023-12-18 19:10
 */
@SuppressWarnings("java:S2176")
public class SimpleForwardingServerCallListener<S>
		extends ForwardingServerCallListener.SimpleForwardingServerCallListener<S> {

	protected SimpleForwardingServerCallListener(ServerCall.Listener<S> delegate) {
		super(delegate);
	}

	@Override
	public void onMessage(S message) {
		onMessageBefore(message);
		super.onMessage(message);
		onMessageAfter(message);
	}

	@Override
	public void onHalfClose() {
		onHalfCloseBefore();
		super.onHalfClose();
		onHalfCloseAfter();
	}

	@Override
	public void onCancel() {
		onCancelBefore();
		super.onCancel();
		onCancelAfter();
	}

	@Override
	public void onComplete() {
		onCompleteBefore();
		super.onComplete();
		onCompleteAfter();
	}

	@Override
	public void onReady() {
		onReadyBefore();
		super.onReady();
		onReadyAfter();
	}

	public void onMessageBefore(S message) {
		//
	}

	public void onMessageAfter(S message) {
		//
	}

	public void onHalfCloseBefore() {
		//
	}

	public void onHalfCloseAfter() {
		//
	}

	public void onCancelBefore() {
		//
	}

	public void onCancelAfter() {
		//
	}

	public void onCompleteBefore() {
		//
	}

	public void onCompleteAfter() {
		//
	}

	public void onReadyBefore() {
		//
	}

	public void onReadyAfter() {
		//
	}

}
