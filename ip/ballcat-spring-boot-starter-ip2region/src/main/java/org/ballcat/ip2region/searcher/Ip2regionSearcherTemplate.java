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

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ballcat.autoconfigure.ip2region.Ip2regionProperties;
import org.ballcat.ip2region.core.IpInfo;
import org.ballcat.ip2region.util.IpInfoUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ResourceLoader;

/**
 * Ip2region 搜索服务实现
 *
 * @author lishangbu 2022/10/16
 */
@RequiredArgsConstructor
public abstract class Ip2regionSearcherTemplate implements DisposableBean, InitializingBean, Ip2regionSearcher {

	protected final ResourceLoader resourceLoader;

	protected final Ip2regionProperties properties;

	protected Searcher searcher;

	@Override
	@SneakyThrows({ IOException.class })
	public IpInfo search(long ip) {
		return IpInfoUtils.toIpInfo(Searcher.long2ip(ip), this.searcher.search(ip));

	}

	@Override
	@SneakyThrows({ Exception.class })
	public IpInfo search(String ip) {
		return IpInfoUtils.toIpInfo(ip, this.searcher.search(ip));
	}

	@Override
	public IpInfo searchQuietly(long ip) {
		try {
			return search(ip);
		}
		catch (Exception e) {
			// do nothing
			return null;
		}
	}

	@Override
	public IpInfo searchQuietly(String ip) {
		try {
			return search(ip);
		}
		catch (Exception e) {
			// do nothing
			return null;
		}
	}

	@Override
	public void destroy() throws Exception {
		if (this.searcher != null) {
			this.searcher.close();
		}
	}

}
