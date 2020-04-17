package com.hccake.ballcat.commom.storage;


import com.hccake.ballcat.commom.storage.aliyun.AliyunOssClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * oss 自动配置类
 * @author Hccake
 */
@AllArgsConstructor
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageAutoConfiguration {
	private final FileStorageProperties properties;

	@Bean
	@ConditionalOnMissingBean(FileStorageClient.class)
	@ConditionalOnProperty(name = "file.storage.type", havingValue = "aliyun")
	FileStorageClient aliyunOssClient() {
		return new AliyunOssClient(
				properties.getEndpoint(),
				properties.getAccessKey(),
				properties.getAccessSecret(),
				properties.getBucketName()
		);
	}

}
