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

package org.ballcat.redis.moudle.bloom;

/**
 * redisBloom 中 BF.INSERT 命令的附属参数 Keyword
 *
 * @author hccake
 */
public enum BloomInsertKeywordEnum {

	/**
	 * 过滤器容量
	 */
	CAPACITY,
	/**
	 * 期望错误率
	 */
	ERROR,
	/**
	 * 当不存在过滤器时不进行创建过滤器，默认会创建
	 */
	NOCREATE,
	/**
	 * 插入过滤器的元素
	 */
	ITEMS

}
