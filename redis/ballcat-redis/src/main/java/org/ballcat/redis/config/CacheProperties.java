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

package org.ballcat.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Hccake 2020/3/20 16:56
 */
@Data
@ConfigurationProperties(prefix = CacheProperties.PREFIX)
public class CacheProperties {

	public static final String PREFIX = "ballcat.redis";

	/**
	 * 通用的key前缀
	 */
	private String keyPrefix = "";

	/**
	 * redis锁 后缀
	 */
	private String lockKeySuffix = "locked";

	/**
	 * 默认分隔符
	 */
	private String delimiter = ":";

	/**
	 * 空值标识
	 */
	private String nullValue = "N_V";

	/**
	 * 默认缓存数据的超时时间(s)
	 */
	private long expireTime = 86400L;

	/**
	 * 默认锁的超时时间(s)
	 */
	private long defaultLockTimeout = 10L;

	@NestedConfigurationProperty
	private KeyEventConfig keyExpiredEvent = new KeyEventConfig();

	@NestedConfigurationProperty
	private KeyEventConfig keyDeletedEvent = new KeyEventConfig();

	@NestedConfigurationProperty
	private KeyEventConfig keySetEvent = new KeyEventConfig();

}
