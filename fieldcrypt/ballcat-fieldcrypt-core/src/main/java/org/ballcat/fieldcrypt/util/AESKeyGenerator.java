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

package org.ballcat.fieldcrypt.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 密钥生成工具类. 测试使用。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class AESKeyGenerator {

	private AESKeyGenerator() {
	}

	/**
	 * 生成随机的 AES-256 密钥（32字节）.
	 * @return Base64 编码的密钥字符串
	 */
	public static String generateAES256Key() {
		// AES-256 需要 32 字节（256位）的密钥
		byte[] keyBytes = new byte[32];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(keyBytes);

		// 转换为 Base64 字符串
		return Base64.getEncoder().encodeToString(keyBytes);
	}

	/**
	 * 生成 AES-256 密钥并返回字节数组和Base64字符串.
	 * @return 包含原始字节和Base64字符串的数组
	 */
	public static Object[] generateAES256KeyWithBytes() {
		byte[] keyBytes = new byte[32];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(keyBytes);

		String base64Key = Base64.getEncoder().encodeToString(keyBytes);
		return new Object[] { keyBytes, base64Key };
	}

}
