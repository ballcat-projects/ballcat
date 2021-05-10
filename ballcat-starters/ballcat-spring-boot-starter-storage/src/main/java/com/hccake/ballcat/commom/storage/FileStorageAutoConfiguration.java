package com.hccake.ballcat.commom.storage;

import com.aliyun.oss.OSS;
import com.hccake.ballcat.commom.storage.client.AliyunOssClient;
import com.hccake.ballcat.commom.storage.client.AwsOssClient;
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
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageAutoConfiguration {

	private final FileStorageProperties properties;

	@Bean
	@ConditionalOnMissingBean(FileStorageClient.class)
	@ConditionalOnClass(OSS.class)
	public FileStorageClient aliyunOssClient() {
		return new AliyunOssClient(properties.getEndpoint(), properties.getAccessKey(), properties.getAccessSecret(),
				properties.getBucketName(), properties.getRootPath());
	}

	@Bean
	@ConditionalOnClass(S3Client.class)
	@ConditionalOnMissingBean(FileStorageClient.class)
	public FileStorageClient awsOssClient() {
		return new AwsOssClient(properties.getEndpoint(), properties.getAccessKey(), properties.getAccessSecret(),
				properties.getBucketName(), properties.getRootPath());
	}

}
