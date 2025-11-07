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

package org.ballcat.fieldcrypt.crypto.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.ballcat.fieldcrypt.core.exception.FieldCryptCryptoException;
import org.ballcat.fieldcrypt.core.exception.FieldCryptErrorCode;
import org.ballcat.fieldcrypt.crypto.CryptoContext;
import org.ballcat.fieldcrypt.crypto.spi.CryptoAlgorithm;

/**
 * 确定性 AES/CBC 加密（固定 IV）。用于需要等值匹配的场景. 这里是固定 AES KEY + 固定 IV。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class AesCbcFixedIvCryptoAlgorithm implements CryptoAlgorithm {

	/**
	 * 算法名称。
	 */
	public static final String ALGO_NAME = "AES_CBC_FIXED_IV";

	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	// 固定 IV。
	private static final IvParameterSpec FIXED_IV = new IvParameterSpec(new byte[] { (byte) 0x01, (byte) 0x02,
			(byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A,
			(byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10 });

	private final SecretKey secretKey;

	public AesCbcFixedIvCryptoAlgorithm(String aesKey) {
		this.secretKey = generateKey(aesKey);
	}

	private static SecretKey generateKey(String aesKey) {
		byte[] dummy = Base64.getDecoder().decode(aesKey);
		return new SecretKeySpec(dummy, "AES");
	}

	@Override
	public String algo() {
		return ALGO_NAME;
	}

	@Override
	public String encrypt(String plaintext, CryptoContext context) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, FIXED_IV);
			byte[] enc = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(enc);
		}
		catch (Exception e) {
			throw FieldCryptCryptoException.of(FieldCryptErrorCode.ALGO_EXEC_ERROR, "encrypt", e)
				.withDetail("algo", ALGO_NAME);
		}
	}

	@Override
	public String decrypt(String ciphertext, CryptoContext context) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, this.secretKey, FIXED_IV);
			byte[] dec = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
			return new String(dec, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw FieldCryptCryptoException.of(FieldCryptErrorCode.ALGO_EXEC_ERROR, "decrypt", e)
				.withDetail("algo", ALGO_NAME);
		}
	}

}
