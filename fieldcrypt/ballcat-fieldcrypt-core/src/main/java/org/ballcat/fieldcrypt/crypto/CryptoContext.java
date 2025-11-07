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

import lombok.Getter;

/**
 * 加/解密操作上下文对象。 目前包含算法标识与透传参数, 留作扩展点。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Getter
public class CryptoContext {

	/**
	 * 算法名称。
	 */
	private final String algo;

	/**
	 * 来自注解或解析元数据的原始参数字符串。
	 */
	private final String params;

	public CryptoContext(String algo, String params) {
		this.algo = algo;
		this.params = params;
	}

}
