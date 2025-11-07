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

package org.ballcat.fieldcrypt.crypto;

import java.util.Objects;

import org.ballcat.fieldcrypt.core.exception.FieldCryptCryptoException;
import org.ballcat.fieldcrypt.core.exception.FieldCryptErrorCode;
import org.ballcat.fieldcrypt.crypto.spi.CryptoAlgorithm;

/**
 * 默认的加解密引擎实现.
 *
 * @author Hccake
 * @since 2.0.0
 */
public class DefaultCryptoEngine implements CryptoEngine {

	private static final String PREFIX = "ENC:";

	private final CryptoAlgorithmRegistry registry;

	private volatile String defaultAlgo;

	public DefaultCryptoEngine(CryptoAlgorithmRegistry registry) {
		this.registry = Objects.requireNonNull(registry, "registry");
	}

	@Override
	public String encrypt(String plain, CryptoContext ctx) {
		if (plain == null) {
			return null;
		}
		// 幂等：已带前缀的字符串视为已加密，直接返回
		if (plain.startsWith(PREFIX)) {
			return plain;
		}
		String base = resolve(ctx == null ? null : ctx.getAlgo()).encrypt(plain, ctx);
		return PREFIX + base;
	}

	@Override
	public String decrypt(String cipher, CryptoContext ctx) {
		if (cipher == null) {
			return null;
		}
		if (cipher.startsWith(PREFIX)) {
			String body = cipher.substring(PREFIX.length());
			return resolve(ctx == null ? null : ctx.getAlgo()).decrypt(body, ctx);
		}
		// 无前缀：视为明文或未加密数据，直接返回
		return cipher;
	}

	@Override
	public void setDefaultAlgo(String defaultAlgo) {
		if (defaultAlgo == null || defaultAlgo.trim().isEmpty()) {
			this.defaultAlgo = null;
			return;
		}
		if (!this.registry.contains(defaultAlgo)) {
			throw FieldCryptCryptoException.of(FieldCryptErrorCode.ALGO_NOT_FOUND, "setDefaultAlgo")
				.withDetail("algo", defaultAlgo);
		}
		this.defaultAlgo = defaultAlgo;
	}

	@Override
	public String getDefaultAlgo() {
		return this.defaultAlgo;
	}

	private CryptoAlgorithm resolve(String algo) {
		String key = (algo == null || algo.isEmpty()) ? this.defaultAlgo : algo;
		CryptoAlgorithm svc = (key == null) ? null : this.registry.get(key);
		if (svc == null) {
			throw FieldCryptCryptoException.of(FieldCryptErrorCode.ALGO_NOT_FOUND, "resolve")
				.withDetail("algo", String.valueOf(key));
		}
		return svc;
	}

}
