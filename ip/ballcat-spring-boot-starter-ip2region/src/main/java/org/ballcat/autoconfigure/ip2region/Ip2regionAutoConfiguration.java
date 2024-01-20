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

package org.ballcat.autoconfigure.ip2region;

import org.ballcat.ip2region.core.CacheType;
import org.ballcat.ip2region.exception.Ip2regionException;
import org.ballcat.ip2region.searcher.CacheVectorIndexIp2regionSearcher;
import org.ballcat.ip2region.searcher.CacheXdbFileIp2regionSearcher;
import org.ballcat.ip2region.searcher.Ip2regionSearcher;
import org.ballcat.ip2region.searcher.NoneCacheIp2regionSearcher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/**
 * Ip2region自动配置
 *
 * @author lishangbu 2022/10/16
 */
@AutoConfiguration
@EnableConfigurationProperties(Ip2regionProperties.class)
public class Ip2regionAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Ip2regionSearcher ip2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		CacheType cacheType = properties.getCacheType();
		switch (cacheType) {
			case XDB:
				return new CacheXdbFileIp2regionSearcher(resourceLoader, properties);
			case NONE:
				return new NoneCacheIp2regionSearcher(resourceLoader, properties);
			case VECTOR_INDEX:
				return new CacheVectorIndexIp2regionSearcher(resourceLoader, properties);
			default:
				throw new Ip2regionException("Property `cache-type` is invalid");
		}
	}

}
