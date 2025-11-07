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

import org.ballcat.fieldcrypt.crypto.impl.AesCbcFixedIvCryptoAlgorithm;
import org.ballcat.fieldcrypt.crypto.spi.CryptoAlgorithm;
import org.ballcat.fieldcrypt.util.AESKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCryptoEnginePrefixTest {

	private DefaultCryptoEngine engine;

	/**
	 * 32 bytes base64。
	 */
	private static final String BASE64_KEY = AESKeyGenerator.generateAES256Key();

	@BeforeEach
	void setup() {
		CryptoAlgorithmRegistry registry = new CryptoAlgorithmRegistry();
		CryptoAlgorithm algo = new AesCbcFixedIvCryptoAlgorithm(BASE64_KEY);
		registry.register(algo);
		this.engine = new DefaultCryptoEngine(registry);
		this.engine.setDefaultAlgo(algo.algo());
	}

	@Test
	void encryptAddsPrefixAndDecryptStrips() {
		String plain = "hello";
		String cipherWithPrefix = this.engine.encrypt(plain, new CryptoContext("", ""));
		assertNotNull(cipherWithPrefix);
		assertTrue(cipherWithPrefix.startsWith("ENC:"));
		String back = this.engine.decrypt(cipherWithPrefix, new CryptoContext("", ""));
		assertEquals(plain, back);
	}

	@Test
	void decryptWithoutPrefixReturnsAsIs() {
		String input = "hello";
		String out = this.engine.decrypt(input, new CryptoContext("", ""));
		assertEquals(input, out);
	}

	@Test
	void encryptIsIdempotentWhenAlreadyPrefixed() {
		String already = "ENC:anything";
		String again = this.engine.encrypt(already, new CryptoContext("", ""));
		assertSame(already, again);
	}

}
