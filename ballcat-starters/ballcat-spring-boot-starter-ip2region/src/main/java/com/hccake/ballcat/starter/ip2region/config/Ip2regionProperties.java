package com.hccake.ballcat.starter.ip2region.config;

import com.hccake.ballcat.starter.ip2region.core.CacheType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * IP2region配置项
 *
 * @author lishangbu
 * @date 2022/10/16
 */
@ConfigurationProperties(Ip2regionProperties.PREFIX)
@Data
public class Ip2regionProperties {

	public static final String PREFIX = "ballcat.ip2region";

	/**
	 * ip2region.xdb 文件路径，默认： classpath:ip2region/ip2region.xdb
	 */
	private String fileLocation = "classpath:ip2region/ip2region.xdb";

	/**
	 * 默认采用缓存整个XDB文件的搜索方式
	 */
	private CacheType cacheType = CacheType.XDB;

}
