package com.hccake.common.i18n.cache;

/**
 * 缓存服务 规范国际化的缓存
 *
 * @author Yakir
 */
public interface CacheService {

	/**
	 * 设置过期时间永不过期
	 * @param cacheKey
	 * @param cacheValue
	 */
	void put(String cacheKey, String cacheValue);

	/**
	 * 设置缓存
	 * @param cacheKey 缓存key
	 * @param cacheValue 值
	 * @param expireTime 秒(S)为单位
	 */
	void put(String cacheKey, String cacheValue, Long expireTime);

	/**
	 * 根据key 得到数据
	 * @param cacheKey 缓存key
	 * @return string
	 */
	String get(String cacheKey);

	/**
	 * 根据key删除
	 * @param cacheKey 缓存key
	 */
	void del(String cacheKey);

}
