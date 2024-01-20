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

package org.ballcat.ip2region.core;

/**
 * 搜索服务实现
 *
 * @author lishangbu 2022/10/16
 */
public enum CacheType {

	/**
	 * 不缓存
	 * <p>
	 * 完全基于文件的查询
	 * </p>
	 */
	NONE,
	/**
	 * 缓存 VectorIndex 索引
	 * <p>
	 * 提前从 xdb 文件中加载出来 VectorIndex 数据，然后全局缓存，每次创建 Searcher 对象的时候使用全局的 VectorIndex
	 * 缓存可以减少一次固定的 IO 操作，从而加速查询，减少 IO 压力
	 * </p>
	 */
	VECTOR_INDEX,
	/**
	 * 缓存整个 xdb 数据
	 * <p>
	 * 预先加载整个 ip2region.xdb 的数据到内存，然后基于这个数据创建查询对象来实现完全基于文件的查询，类似之前的 memory search
	 * </p>
	 */
	XDB

}
