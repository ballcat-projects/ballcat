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

import org.ballcat.autoconfigure.ip2region.Ip2regionProperties;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 完全基于文件的查询,不缓存,直接检索
 * <p>
 * 参考<a href=
 * "https://gitee.com/lionsoul/ip2region/tree/master/binding/java">ip2region</a>魔改
 *
 * @author lishangbu 2022/10/16
 */
public class NoneCacheIp2regionSearcher extends Ip2regionSearcherTemplate {

	public NoneCacheIp2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		super(resourceLoader, properties);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = this.resourceLoader.getResource(this.properties.getFileLocation());
		this.searcher = Searcher.newWithFileOnly(resource.getFile().getPath());
	}

}
