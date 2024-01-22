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

package org.ballcat.springsecurity.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.ballcat.common.util.AesUtils;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * 前后端交互中密码使用 AES 加密，模式: CBC，padding: PKCS5，偏移量暂不定制和密钥相同。 <br/>
 * 服务端OAuth2中，密码使用BCrypt方式加密
 *
 * @author Hccake 2019/9/25 15:14
 */
public final class PasswordUtils {

	private PasswordUtils() {
	}

	/**
	 * 创建一个密码加密的代理，方便后续切换密码的加密算法
	 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
	 * @return DelegatingPasswordEncoder
	 */
	@SuppressWarnings("deprecation")
	public static PasswordEncoder createDelegatingPasswordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>(10);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		encoders.put(encodingId, bCryptPasswordEncoder);
		encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
		encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256",
				new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		encoders.put("argon2", new Argon2PasswordEncoder());
		DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);

		// 设置默认的密码解析器，以便兼容历史版本的密码
		delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
		return delegatingPasswordEncoder;
	}

	/**
	 * 将前端传递过来的密文解密为明文
	 * @param aesPass AES加密后的密文
	 * @param secretKey 密钥
	 * @return 明文密码
	 */
	public static String decodeAES(String aesPass, String secretKey) throws GeneralSecurityException {
		final byte[] secretKeyBytes = secretKey.getBytes();
		final byte[] passBytes = java.util.Base64.getDecoder().decode(aesPass);
		final byte[] bytes = AesUtils.cbcDecrypt(passBytes, secretKeyBytes, secretKeyBytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * 将明文密码加密为密文
	 * @param password 明文密码
	 * @param secretKey 密钥
	 * @return AES加密后的密文
	 */
	public static String encodeAESBase64(String password, String secretKey) throws GeneralSecurityException {
		final byte[] secretKeyBytes = secretKey.getBytes();
		final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
		final byte[] bytes = AesUtils.cbcEncrypt(passwordBytes, secretKeyBytes, secretKeyBytes);
		return java.util.Base64.getEncoder().encodeToString(bytes);
	}

}
