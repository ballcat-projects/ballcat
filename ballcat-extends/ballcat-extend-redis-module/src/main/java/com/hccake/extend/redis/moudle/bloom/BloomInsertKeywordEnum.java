package com.hccake.extend.redis.moudle.bloom;

/**
 * redisBloom 中 BF.INSERT 命令的附属参数 Keyword
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
	ITEMS;

}