package com.hccake.ballcat.common.redis.config;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/20 16:56
 * 缓存配置持有者，方便静态获取配置信息
 */
public class CachePropertiesHolder {
	private static CacheProperties cacheProperties;

	public void setCacheProperties(CacheProperties cacheProperties) {
		CachePropertiesHolder.cacheProperties = cacheProperties;
	}

	public static String keyPrefix() {
		return cacheProperties.getKeyPrefix();
	}

	public static String lockKeySuffix() {
        return cacheProperties.getLockKeySuffix();
    }

    public static String delimiter() {
        return cacheProperties.getDelimiter();
    }

    public static String nullValue() {
        return cacheProperties.getNullValue();
    }

    public static long expireTime() {
        return cacheProperties.getExpireTime();
    }

    public static long lockedTimeOut() {
        return cacheProperties.getLockedTimeOut();
    }


}
