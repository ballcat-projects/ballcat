package com.hccake.ballcat.starter.ip2region.config;

import com.hccake.ballcat.starter.ip2region.core.CacheType;
import com.hccake.ballcat.starter.ip2region.exception.Ip2regionException;
import com.hccake.ballcat.starter.ip2region.searcher.CacheVectorIndexIp2regionSearcher;
import com.hccake.ballcat.starter.ip2region.searcher.CacheXdbFileIp2regionSearcher;
import com.hccake.ballcat.starter.ip2region.searcher.Ip2regionSearcher;
import com.hccake.ballcat.starter.ip2region.searcher.NoneCacheIp2regionSearcher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/**
 * Ip2region自动配置
 *
 * @author lishangbu
 * @date 2022/10/16
 */
@AutoConfiguration
@EnableConfigurationProperties(Ip2regionProperties.class)
public class Ip2regionAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Ip2regionSearcher ip2regionSearcher(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		CacheType cacheType = properties.getCacheType();
		switch (cacheType) {
			case XDB:
				return new CacheXdbFileIp2regionSearcher(resourceLoader, properties);
			case NONE:
				return new NoneCacheIp2regionSearcher(resourceLoader, properties);
			case VECTOR_INDEX:
				return new CacheVectorIndexIp2regionSearcher(resourceLoader, properties);
			default:
				throw new Ip2regionException("Property `cache-type` is invalid");
		}
	}

}
