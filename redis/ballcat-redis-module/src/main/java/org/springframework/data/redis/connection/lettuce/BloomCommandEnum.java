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

package org.springframework.data.redis.connection.lettuce;

import java.nio.charset.StandardCharsets;

import io.lettuce.core.protocol.ProtocolKeyword;

/**
 * 布隆过滤器的操作命令类
 *
 * @see <a href="https://oss.redislabs.com/redisbloom/Bloom_Commands/">redisbloom command
 * doc</a>
 * @author hccake
 */
public enum BloomCommandEnum implements ProtocolKeyword {

	/**
	 * 创建一个空的 bloomFilter, 可以指定初始容量和预期错误率
	 */
	RESERVE("BF.RESERVE"),
	/**
	 * 将一个元素添加进 bloomFilter，如果 filter 不存在，则创建一个 filter
	 */
	ADD("BF.ADD"),
	/**
	 * 将一个元素或多个元素添加进 bloomFilter，如果 filter 不存在，则创建一个 filter
	 */
	MADD("BF.MADD"),
	/**
	 * 确定一个元素是否在 bloomFilter 中存在
	 */
	EXISTS("BF.EXISTS"),
	/**
	 * 确定一个元素或多个元素是否在 bloomFilter 中存在
	 */
	MEXISTS("BF.MEXISTS"),
	/**
	 * 是 BF.RESERVE 和 BF.ADD 的语法糖。 如果 filter 不存在，则使用相关参数创建一个新的过滤器(参见 BF.RESERVE)。
	 * 接下来，插入所有项目。
	 */
	INSERT("BF.INSERT"),
	/**
	 * 获取 filter 的相关信息
	 */
	INFO("BF.INFO");

	private final String command;

	private final byte[] bytes;

	BloomCommandEnum(String command) {
		this.command = command;
		this.bytes = command().getBytes(StandardCharsets.US_ASCII);
	}

	public String command() {
		return this.command;
	}

	@Override
	public byte[] getBytes() {
		return this.bytes;
	}

}
