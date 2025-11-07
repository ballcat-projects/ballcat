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

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.ballcat.fieldcrypt.crypto.spi.CryptoAlgorithm;

/**
 * 纯注册中心：只负责算法实现的注册与查找，不处理默认算法或路由。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class CryptoAlgorithmRegistry {

	private final Map<String, CryptoAlgorithm> services = new ConcurrentHashMap<>();

	public void register(CryptoAlgorithm algo) {
		Objects.requireNonNull(algo, "algo");
		String name = algo.algo();
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("CryptoAlgorithm.algo() must not be empty");
		}
		this.services.put(name, algo);
	}

	public CryptoAlgorithm get(String name) {
		return this.services.get(name);
	}

	public boolean contains(String name) {
		return this.services.containsKey(name);
	}

	public Collection<CryptoAlgorithm> all() {
		return this.services.values();
	}

}
