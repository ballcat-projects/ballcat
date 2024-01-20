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

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 幂等属性配置
 *
 * @author lishangbu 2022/10/18
 */
@Setter
@Getter
@ConfigurationProperties(IdempotentProperties.PREFIX)
public class IdempotentProperties {

	public static final String PREFIX = "ballcat.idempotent";

	private KeyStoreType keyStoreType = KeyStoreType.MEMORY;

	/**
	 * 存储方式
	 */
	public enum KeyStoreType {

		/**
		 * 内存存储
		 */
		MEMORY,
		/**
		 * redis存储
		 */
		REDIS

	}

}
