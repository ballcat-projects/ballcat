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

package org.ballcat.common.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密算法：AES
 * <p>
 * <a href=
 * "https://docs.oracle.com/en/java/javase/16/docs/api/java.base/javax/crypto/Cipher.html">支持的AES实现</a>
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/3/6
 */
public final class AesUtils {

	private AesUtils() {
	}

	// 算法名称/工作模式/填充方式
	public static final String AES_CBC_NOPADDING = "AES/CBC/NoPadding";

	public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

	public static final String AES_ECB_NOPADDING = "AES/ECB/NoPadding";

	public static final String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";

	public static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";

	public static byte[] encrypt(String algorithm, byte[] data, Key key) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static byte[] decrypt(String algorithm, byte[] data, Key key) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	public static byte[] encrypt(String algorithm, byte[] data, byte[] key) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		return encrypt(algorithm, data, getSecretKey(key));
	}

	public static byte[] decrypt(String algorithm, byte[] data, byte[] key) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		return decrypt(algorithm, data, getSecretKey(key));
	}

	public static byte[] encrypt(String algorithm, byte[] data, Key key, AlgorithmParameterSpec parameter)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException {
		return encrypt(algorithm, data, key, parameter, null);
	}

	public static byte[] decrypt(String algorithm, byte[] data, Key key, AlgorithmParameterSpec parameter)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException {
		return decrypt(algorithm, data, key, parameter, null);
	}

	public static byte[] encrypt(String algorithm, byte[] data, byte[] key, byte[] iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException {
		return encrypt(algorithm, data, getSecretKey(key), getIvParameter(iv));
	}

	public static byte[] decrypt(String algorithm, byte[] data, byte[] key, byte[] iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException {
		return decrypt(algorithm, data, getSecretKey(key), getIvParameter(iv));
	}

	public static byte[] encrypt(String algorithm, byte[] data, Key key, AlgorithmParameterSpec parameter, byte[] aad)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, parameter);
		if (aad != null) {
			cipher.updateAAD(aad);
		}
		return cipher.doFinal(data);
	}

	public static byte[] decrypt(String algorithm, byte[] data, Key key, AlgorithmParameterSpec parameter, byte[] aad)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, parameter);
		if (aad != null) {
			cipher.updateAAD(aad);
		}
		return cipher.doFinal(data);
	}

	public static byte[] cbcEncrypt(byte[] data, Key key, IvParameterSpec parameter)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return encrypt(AES_CBC_PKCS5PADDING, data, key, parameter);
	}

	public static byte[] cbcDecrypt(byte[] data, Key key, IvParameterSpec parameter)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return decrypt(AES_CBC_PKCS5PADDING, data, key, parameter);
	}

	public static byte[] cbcEncrypt(byte[] data, byte[] key, byte[] iv)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return encrypt(AES_CBC_PKCS5PADDING, data, key, iv);
	}

	public static byte[] cbcDecrypt(byte[] data, byte[] key, byte[] iv)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return decrypt(AES_CBC_PKCS5PADDING, data, key, iv);
	}

	public static byte[] ecbEncrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return encrypt(AES_ECB_PKCS5PADDING, data, key);
	}

	public static byte[] ecbDecrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return decrypt(AES_ECB_PKCS5PADDING, data, key);
	}

	public static byte[] ecbEncrypt(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return encrypt(AES_ECB_PKCS5PADDING, data, key);
	}

	public static byte[] ecbDecrypt(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return decrypt(AES_ECB_PKCS5PADDING, data, key);
	}

	public static byte[] gcmEncrypt(byte[] data, Key key, GCMParameterSpec parameter)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return gcmEncrypt(data, key, parameter, null);
	}

	public static byte[] gcmDecrypt(byte[] data, Key key, GCMParameterSpec parameter)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return gcmDecrypt(data, key, parameter, null);
	}

	public static byte[] gcmEncrypt(byte[] data, byte[] key, byte[] iv, int tagLength)
			throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchPaddingException {
		return gcmEncrypt(data, key, iv, null, tagLength);
	}

	public static byte[] gcmDecrypt(byte[] data, byte[] key, byte[] iv, int tagLength)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return gcmDecrypt(data, key, iv, null, tagLength);
	}

	public static byte[] gcmEncrypt(byte[] data, Key key, GCMParameterSpec parameter, byte[] aad)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return encrypt(AES_GCM_NOPADDING, data, key, parameter, aad);
	}

	public static byte[] gcmDecrypt(byte[] data, Key key, GCMParameterSpec parameter, byte[] aad)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return decrypt(AES_GCM_NOPADDING, data, key, parameter, aad);
	}

	public static byte[] gcmEncrypt(byte[] data, byte[] key, byte[] iv, byte[] aad, int tagLength)
			throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchPaddingException {
		return gcmEncrypt(data, getSecretKey(key), getGCMParameterSpec(iv, tagLength), aad);
	}

	public static byte[] gcmDecrypt(byte[] data, byte[] key, byte[] iv, byte[] aad, int tagLength)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return gcmDecrypt(data, getSecretKey(key), getGCMParameterSpec(iv, tagLength), aad);
	}

	public static byte[] gcmDecrypt(byte[] ciphertext, byte[] tag, byte[] key, byte[] iv, byte[] aad)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

		// concatenate two byte arrays
		byte[] data = new byte[ciphertext.length + tag.length];
		System.arraycopy(ciphertext, 0, data, 0, ciphertext.length);
		System.arraycopy(tag, 0, data, ciphertext.length, tag.length);

		return gcmDecrypt(data, key, iv, aad, tag.length * 8);
	}

	public static byte[] gcmDecrypt(byte[] ciphertext, byte[] tag, byte[] key, byte[] iv)
			throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		return gcmDecrypt(ciphertext, tag, key, iv, null);
	}

	public static byte[] getGcmCiphertext(byte[] data, int tagLength) {
		return Arrays.copyOfRange(data, 0, data.length - tagLength / 8);
	}

	public static byte[] getGcmAuthenticationTag(byte[] data, int tagLength) {
		return Arrays.copyOfRange(data, data.length - tagLength / 8, data.length);
	}

	/**
	 * 生成 AES 密钥，字节数必须为 16 字节、24 字节或 32 字节
	 */
	public static Key getSecretKey(byte[] key) {
		return new SecretKeySpec(key, "AES");
	}

	public static IvParameterSpec getIvParameter(byte[] iv) {
		return new IvParameterSpec(iv);
	}

	public static GCMParameterSpec getGCMParameterSpec(int authenticationTagLength, byte[] iv) {
		return new GCMParameterSpec(authenticationTagLength, iv);
	}

	/**
	 * @param tagLength authentication tag 比特位数，必须是 128、120、112、104、96 之一
	 */
	public static GCMParameterSpec getGCMParameterSpec(byte[] iv, int tagLength) {
		return getGCMParameterSpec(tagLength, iv);
	}

}
