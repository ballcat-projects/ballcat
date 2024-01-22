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

import lombok.Data;
import org.ballcat.ip2region.core.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * IP2region配置项
 *
 * @author lishangbu 2022/10/16
 */
@ConfigurationProperties(Ip2regionProperties.PREFIX)
@Data
public class Ip2regionProperties {

	public static final String PREFIX = "ballcat.ip2region";

	/**
	 * ip2region.xdb 文件路径，默认： classpath:ip2region/ip2region.xdb
	 */
	private String fileLocation = "classpath:ip2region/ip2region.xdb";

	/**
	 * 默认采用缓存整个XDB文件的搜索方式
	 */
	private CacheType cacheType = CacheType.XDB;

}
