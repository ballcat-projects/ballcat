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

package org.ballcat.fieldcrypt.crypto.spi;

import org.ballcat.fieldcrypt.crypto.CryptoContext;

/**
 * 加解密算法接口，一个具体实现对应一种算法。
 *
 * @author Hccake
 * @since 2.0.0
 */
public interface CryptoAlgorithm {

	/**
	 * 返回算法标识（如 AES_GCM），用于注册与查找
	 */
	String algo();

	/**
	 * 加密明文
	 * @param plain 明文
	 * @param ctx 上下文
	 * @return 密文
	 */
	String encrypt(String plain, CryptoContext ctx);

	/**
	 * 解密密文
	 * @param cipher 密文
	 * @param ctx 上下文
	 * @return 明文
	 */
	String decrypt(String cipher, CryptoContext ctx);

}
