package com.hccake.ballcat.common.oss;

import com.hccake.ballcat.common.oss.prefix.DefaultObjectPrefixConverter;
import com.hccake.ballcat.common.oss.prefix.ObjectPrefixConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * oss 自动配置类
 *
 * @author Hccake
 */
@AutoConfiguration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class OssAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(OssTemplate.class)
	public OssTemplate ossTemplate(OssProperties properties, ObjectPrefixConverter objectPrefixConverter) {
		if (objectPrefixConverter.match()) {
			return new GlobalObjectPrefixOssTemplate(properties, objectPrefixConverter);
		}
		else {
			return new DefaultOssTemplate(properties);
		}
	}

	@Bean
	@ConditionalOnMissingBean(ObjectPrefixConverter.class)
	public ObjectPrefixConverter objectPrefixConverter(OssProperties properties) {
		return new DefaultObjectPrefixConverter(properties);
	}

	/**
	 * OSS客户端，单纯用来兼容老版本实现
	 * @param ossTemplate
	 * @param objectPrefixConverter
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(OssClient.class)
	public OssClient ossClient(OssTemplate ossTemplate, ObjectPrefixConverter objectPrefixConverter) {
		return new OssClient(ossTemplate, objectPrefixConverter);
	}

}
