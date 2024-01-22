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

package org.ballcat.common.core.https;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.ballcat.common.core.constant.HttpsConstants;
import org.ballcat.common.util.ArrayUtils;

/**
 * 用于兼容 android 使用
 *
 * @author lingting
 */
public class CompatibleSSLFactory extends SSLSocketFactory {

	private final String[] protocols;

	private final SSLSocketFactory factory;

	public CompatibleSSLFactory(String... protocols) throws NoSuchAlgorithmException, KeyManagementException {
		this(HttpsConstants.TLS, HttpsConstants.KEY_MANAGERS, HttpsConstants.TRUST_MANAGERS, new SecureRandom(),
				protocols);
	}

	public CompatibleSSLFactory(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers,
			SecureRandom secureRandom, String... protocols) throws NoSuchAlgorithmException, KeyManagementException {
		this.protocols = protocols;
		final SSLContext context = SSLContext.getInstance(protocol);
		context.init(keyManagers, trustManagers, secureRandom);
		this.factory = context.getSocketFactory();
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return this.factory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return this.factory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket() throws IOException {
		return enabledProtocols(this.factory.createSocket());
	}

	@Override
	public Socket createSocket(Socket socket, InputStream inputStream, boolean b) throws IOException {
		return enabledProtocols(this.factory.createSocket(socket, inputStream, b));
	}

	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
		return enabledProtocols(this.factory.createSocket(socket, s, i, b));
	}

	@Override
	public Socket createSocket(String s, int i) throws IOException {
		return enabledProtocols(this.factory.createSocket(s, i));
	}

	@Override
	public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException {
		return enabledProtocols(this.factory.createSocket(s, i, inetAddress, i1));
	}

	@Override
	public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
		return enabledProtocols(this.factory.createSocket(inetAddress, i));
	}

	@Override
	public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
		return enabledProtocols(this.factory.createSocket(inetAddress, i, inetAddress1, i1));
	}

	private Socket enabledProtocols(Socket socket) {
		if (!ArrayUtils.isEmpty(this.protocols) && socket instanceof SSLSocket) {
			((SSLSocket) socket).setEnabledProtocols(this.protocols);
		}
		return socket;
	}

}
