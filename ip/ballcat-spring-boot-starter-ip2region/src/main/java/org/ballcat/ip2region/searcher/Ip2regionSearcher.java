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

package org.ballcat.ip2region.searcher;

import org.ballcat.ip2region.core.IpInfo;
import org.springframework.lang.Nullable;

/**
 * IP 搜索
 *
 * @author lishangbu 2022/10/16
 */
public interface Ip2regionSearcher {

	/**
	 * ip 位置 搜索
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo search(@Nullable long ip);

	/**
	 * ip 位置 搜索
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo search(@Nullable String ip);

	/**
	 * 静默ip 位置 搜索
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo searchQuietly(@Nullable long ip);

	/**
	 * 静默ip 位置 搜索
	 * @param ip ip
	 * @return 位置
	 */
	@Nullable
	IpInfo searchQuietly(@Nullable String ip);

}
