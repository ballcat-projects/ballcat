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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Bloom Filter Insert 命令的参数 copyFrom <a href=
 * "https://github.com/RedisBloom/JRedisBloom/blob/HEAD/src/main/java/io/rebloom/client/InsertOptions.java">RedisBloom
 * InsertOptions</a>
 */
public class BloomInsertOptions {

	private final List<String> options = new ArrayList<>();

	public BloomInsertOptions() {
	}

	public BloomInsertOptions(long capacity, double errorRate) {
		this.capacity(capacity).error(errorRate);
	}

	/**
	 * If specified, should be followed by the desired capacity for the filter to be
	 * created
	 * @param capacity 过滤器大小
	 * @return InsertOptions
	 */
	public BloomInsertOptions capacity(final long capacity) {
		this.options.add(BloomInsertKeywordEnum.CAPACITY.name());
		this.options.add(String.valueOf(capacity));
		return this;
	}

	/**
	 * If specified, should be followed by the the error ratio of the newly created filter
	 * if it does not yet exist
	 * @param errorRate 期望错误率
	 * @return InsertOptions
	 */
	public BloomInsertOptions error(final double errorRate) {
		this.options.add(BloomInsertKeywordEnum.ERROR.name());
		this.options.add(String.valueOf(errorRate));
		return this;
	}

	/**
	 * If specified, indicates that the filter should not be created if it does not
	 * already exist It is an error to specify NOCREATE together with either CAPACITY or
	 * ERROR .
	 * @return InsertOptions
	 */
	public BloomInsertOptions nocreate() {
		this.options.add(BloomInsertKeywordEnum.NOCREATE.name());
		return this;
	}

	public Collection<String> getOptions() {
		return Collections.unmodifiableCollection(this.options);
	}

}
