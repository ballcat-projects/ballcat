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

package org.ballcat.autoconfigure.idempotent;

import lombok.RequiredArgsConstructor;
import org.ballcat.idempotent.IdempotentAspect;
import org.ballcat.idempotent.key.generator.DefaultIdempotentKeyGenerator;
import org.ballcat.idempotent.key.generator.IdempotentKeyGenerator;
import org.ballcat.idempotent.key.store.IdempotentKeyStore;
import org.ballcat.idempotent.key.store.InMemoryIdempotentKeyStore;
import org.ballcat.idempotent.key.store.RedisIdempotentKeyStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 幂等自动装配
 *
 * @author lishangbu 2022/10/18
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {

	/**
	 * 默认的幂等前缀生成器
	 * @return 幂等Key生成器
	 */
	@Bean
	@ConditionalOnMissingBean
	public IdempotentKeyGenerator idempotentKeyGenerator() {
		return new DefaultIdempotentKeyGenerator();
	}

	/**
	 * 默认的幂等键存储器
	 * @param properties 幂等属性配置
	 * @return 幂等Key存储
	 */
	@Bean
	@ConditionalOnMissingBean
	public IdempotentKeyStore idempotentKeyStore(IdempotentProperties properties) {
		IdempotentProperties.KeyStoreType keyStoreType = properties.getKeyStoreType();
		if (keyStoreType.equals(IdempotentProperties.KeyStoreType.REDIS)) {
			return new RedisIdempotentKeyStore();
		}
		else {
			return new InMemoryIdempotentKeyStore();
		}
	}

	/**
	 * 幂等切面
	 * @param idempotentKeyStore 幂等key仓库
	 * @return IdempotentAspect
	 */
	@Bean
	public IdempotentAspect idempotentAspect(IdempotentKeyStore idempotentKeyStore,
			IdempotentKeyGenerator idempotentKeyGenerator) {
		return new IdempotentAspect(idempotentKeyStore, idempotentKeyGenerator);
	}

}
