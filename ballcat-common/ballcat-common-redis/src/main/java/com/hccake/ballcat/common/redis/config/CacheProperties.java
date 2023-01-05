package com.hccake.ballcat.common.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hccake 2020/3/20 16:56
 */
@Data
@ConfigurationProperties(prefix = "ballcat.redis")
public class CacheProperties {

	/**
	 * 通用的key前缀
	 */
	private String keyPrefix = "";

	/**
	 * redis锁 后缀
	 */
	private String lockKeySuffix = "locked";

	/**
	 * 默认分隔符
	 */
	private String delimiter = ":";

	/**
	 * 空值标识
	 */
	private String nullValue = "N_V";

	/**
	 * 默认缓存数据的超时时间(s)
	 */
	private long expireTime = 86400L;

	/**
	 * 默认锁的超时时间(s)
	 */
	private long defaultLockTimeout = 10L;

}
