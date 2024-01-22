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

package org.ballcat.redis.prefix;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * redis key前缀生成器
 *
 * @author huyuanzhi
 */
public interface IRedisPrefixConverter {

	Logger LOGGER = LoggerFactory.getLogger(IRedisPrefixConverter.class);

	/**
	 * 生成前缀
	 * @return 前缀
	 */
	String getPrefix();

	/**
	 * 前置匹配，是否走添加前缀规则
	 * @return 是否匹配
	 */
	boolean match();

	/**
	 * 去除key前缀
	 * @param bytes key字节数组
	 * @return 原始key
	 */
	default byte[] unwrap(byte[] bytes) {
		if (!match() || bytes == null || bytes.length == 0) {
			return bytes;
		}
		String prefix = getPrefix();
		if (!StringUtils.hasText(prefix)) {
			LOGGER.warn("prefix converter is enabled,but method getPrefix returns blank result,check your implement!");
			return bytes;
		}
		byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		int prefixLen = prefixBytes.length;
		int wrapLen = bytes.length;
		int originLen = wrapLen - prefixLen;
		byte[] originBytes = new byte[originLen];
		System.arraycopy(bytes, prefixLen, originBytes, 0, originLen);
		return originBytes;
	}

	/**
	 * 给key加上固定前缀
	 * @param bytes 原始key字节数组
	 * @return 加前缀之后的key
	 */
	default byte[] wrap(byte[] bytes) {
		if (!match() || bytes == null || bytes.length == 0) {
			return bytes;
		}
		String prefix = getPrefix();
		if (!StringUtils.hasText(prefix)) {
			LOGGER.warn("prefix converter is enabled,but method getPrefix returns blank result,check your implement!");
			return bytes;
		}
		byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		int prefixLen = prefixBytes.length;
		int originLen = bytes.length;
		byte[] wrapBytes = new byte[prefixLen + originLen];
		System.arraycopy(prefixBytes, 0, wrapBytes, 0, prefixLen);
		System.arraycopy(bytes, 0, wrapBytes, prefixLen, originLen);
		return wrapBytes;
	}

}
