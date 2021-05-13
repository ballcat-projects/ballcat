package com.hccake.ballcat.commom.oss;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * oss 自动配置类
 *
 * @author Hccake
 */
@AllArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

	@Bean
	@ConditionalOnClass(S3Client.class)
	@ConditionalOnMissingBean(OssClient.class)
	public OssClient ossClient(OssProperties properties) {
		return new OssClient(properties);
	}

}
