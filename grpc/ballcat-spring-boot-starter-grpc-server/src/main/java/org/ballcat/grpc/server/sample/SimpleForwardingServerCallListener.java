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
