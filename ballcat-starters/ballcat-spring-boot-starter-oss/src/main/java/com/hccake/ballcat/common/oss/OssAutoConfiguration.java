package com.hccake.ballcat.common.oss;

import com.hccake.ballcat.common.oss.prefix.DefaultObjectKeyPrefixConverter;
import com.hccake.ballcat.common.oss.prefix.ObjectKeyPrefixConverter;
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

	/**
	 * OSS操作模板，单纯用来兼容老版本实现
	 * @param properties 属性配置
	 * @param objectKeyPrefixConverter S3对象全局键前缀转换器
	 * @return OssTemplate
	 */
	@Bean
	@ConditionalOnMissingBean(OssTemplate.class)
	public OssTemplate ossTemplate(OssProperties properties, ObjectKeyPrefixConverter objectKeyPrefixConverter) {
		if (objectKeyPrefixConverter.match()) {
			return new ObjectWithGlobalKeyPrefixOssTemplate(properties, objectKeyPrefixConverter);
		}
		else {
			return new DefaultOssTemplate(properties);
		}
	}

	/**
	 * S3属性配置
	 * @param properties OSS属性配置文件
	 * @return S3对象全局键前缀转换器
	 */
	@Bean
	@ConditionalOnMissingBean(ObjectKeyPrefixConverter.class)
	public ObjectKeyPrefixConverter objectPrefixConverter(OssProperties properties) {
		return new DefaultObjectKeyPrefixConverter(properties);
	}

	/**
	 * OSS客户端，单纯用来兼容老版本实现
	 * @param ossTemplate oss操作模板
	 * @param objectKeyPrefixConverter S3对象全局键前缀转换器
	 * @return OssClient
	 */
	@Bean
	@ConditionalOnMissingBean(OssClient.class)
	public OssClient ossClient(OssTemplate ossTemplate, ObjectKeyPrefixConverter objectKeyPrefixConverter) {
		return new OssClient(ossTemplate, objectKeyPrefixConverter);
	}

}
