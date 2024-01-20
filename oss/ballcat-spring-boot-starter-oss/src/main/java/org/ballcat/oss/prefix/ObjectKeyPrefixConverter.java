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

package org.ballcat.oss.prefix;

/**
 * 存储对象 key前缀生成器
 *
 * @author lishangbu 2022/10/23
 */
public interface ObjectKeyPrefixConverter {

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
	 * @param key key字节数组
	 * @return 原始key
	 */
	String unwrap(String key);

	/**
	 * 给key加上固定前缀
	 * @param key 原始key字节数组
	 * @return 加前缀之后的key
	 */
	String wrap(String key);

}
