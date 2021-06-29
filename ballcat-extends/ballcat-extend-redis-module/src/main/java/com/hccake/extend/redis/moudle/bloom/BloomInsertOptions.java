package com.hccake.extend.redis.moudle.bloom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Bloom Filter Insert 命令的参数 copyFrom
 * https://github.com/RedisBloom/JRedisBloom/blob/HEAD/src/main/java/io/rebloom/client/InsertOptions.java
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
		options.add(BloomInsertKeywordEnum.CAPACITY.name());
		options.add(String.valueOf(capacity));
		return this;
	}

	/**
	 * If specified, should be followed by the the error ratio of the newly created filter
	 * if it does not yet exist
	 * @param errorRate 期望错误率
	 * @return InsertOptions
	 */
	public BloomInsertOptions error(final double errorRate) {
		options.add(BloomInsertKeywordEnum.ERROR.name());
		options.add(String.valueOf(errorRate));
		return this;
	}

	/**
	 * If specified, indicates that the filter should not be created if it does not
	 * already exist It is an error to specify NOCREATE together with either CAPACITY or
	 * ERROR .
	 * @return InsertOptions
	 */
	public BloomInsertOptions nocreate() {
		options.add(BloomInsertKeywordEnum.NOCREATE.name());
		return this;
	}

	public Collection<String> getOptions() {
		return Collections.unmodifiableCollection(options);
	}

}
