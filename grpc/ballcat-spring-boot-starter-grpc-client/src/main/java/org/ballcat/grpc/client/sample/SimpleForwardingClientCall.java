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
