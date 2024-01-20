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

/**
 * 缓存配置持有者，方便静态获取配置信息
 *
 * @author Hccake 2020/3/20 16:56
 */
public final class CachePropertiesHolder {

	private static final CachePropertiesHolder INSTANCE = new CachePropertiesHolder();

	private CacheProperties cacheProperties;

	public void setCacheProperties(CacheProperties cacheProperties) {
		INSTANCE.cacheProperties = cacheProperties;
	}

	private static CacheProperties cacheProperties() {
		return INSTANCE.cacheProperties;
	}

	public static String keyPrefix() {
		return cacheProperties().getKeyPrefix();
	}

	public static String lockKeySuffix() {
		return cacheProperties().getLockKeySuffix();
	}

	public static String delimiter() {
		return cacheProperties().getDelimiter();
	}

	public static String nullValue() {
		return cacheProperties().getNullValue();
	}

	public static long expireTime() {
		return cacheProperties().getExpireTime();
	}

	public static long defaultLockTimeout() {
		return cacheProperties().getDefaultLockTimeout();
	}

}
