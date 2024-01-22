/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.oss;

import java.net.URI;

import org.ballcat.oss.prefix.DefaultObjectKeyPrefixConverter;
import org.ballcat.oss.prefix.ObjectKeyPrefixConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

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
	 * S3客户端
	 * @param ossProperties oss 配置
	 * @return S3Client
	 */
	@Bean
	@ConditionalOnMissingBean(S3Client.class)
	public S3Client s3Client(OssProperties ossProperties) {
		// 构造S3客户端
		return S3Client.builder()
			.credentialsProvider(StaticCredentialsProvider
				.create(AwsBasicCredentials.create(ossProperties.getAccessKey(), ossProperties.getAccessSecret())))
			.region(Region.of(ossProperties.getRegion()))
			.serviceConfiguration(S3Configuration.builder()
				.pathStyleAccessEnabled(ossProperties.getPathStyleAccess())
				.chunkedEncodingEnabled(ossProperties.getChunkedEncoding())
				.build())
			.endpointOverride(URI.create(ossProperties.getEndpoint()))
			.build();
	}

	/**
	 * S3预签名工具
	 * @param ossProperties oss 配置
	 * @return S3Presigner
	 */
	@Bean
	@ConditionalOnMissingBean(S3Presigner.class)
	public S3Presigner s3Presigner(OssProperties ossProperties) {
		return S3Presigner.builder()
			.credentialsProvider(StaticCredentialsProvider
				.create(AwsBasicCredentials.create(ossProperties.getAccessKey(), ossProperties.getAccessSecret())))
			.region(Region.of(ossProperties.getRegion()))
			.serviceConfiguration(S3Configuration.builder()
				.pathStyleAccessEnabled(ossProperties.getPathStyleAccess())
				.chunkedEncodingEnabled(ossProperties.getChunkedEncoding())
				.build())
			.endpointOverride(URI.create(ossProperties.getEndpoint()))
			.build();
	}

	/**
	 * S3高级传输工具
	 * @param ossProperties oss 配置
	 * @return S3TransferManager
	 */
	@Bean
	@ConditionalOnMissingBean(S3TransferManager.class)
	public S3TransferManager s3TransferManager(OssProperties ossProperties) {
		return S3TransferManager.builder()
			.s3Client(S3AsyncClient.builder()
				.credentialsProvider(StaticCredentialsProvider
					.create(AwsBasicCredentials.create(ossProperties.getAccessKey(), ossProperties.getAccessSecret())))
				.region(Region.of(ossProperties.getRegion()))
				.serviceConfiguration(S3Configuration.builder()
					.pathStyleAccessEnabled(ossProperties.getPathStyleAccess())
					.chunkedEncodingEnabled(ossProperties.getChunkedEncoding())
					.build())
				.endpointOverride(URI.create(ossProperties.getEndpoint()))
				.build())
			.build();

	}

	/**
	 * OSS操作模板，单纯用来兼容老版本实现
	 * @param properties 属性配置
	 * @param objectKeyPrefixConverter S3对象全局键前缀转换器
	 * @return OssTemplate
	 */
	@Bean
	@ConditionalOnMissingBean(OssTemplate.class)
	public OssTemplate ossTemplate(OssProperties properties, S3Client s3Client, S3Presigner s3Presigner,
			S3TransferManager s3TransferManager, ObjectKeyPrefixConverter objectKeyPrefixConverter) {
		if (objectKeyPrefixConverter.match()) {
			return new ObjectWithGlobalKeyPrefixOssTemplate(properties, s3Client, s3Presigner, s3TransferManager,
					objectKeyPrefixConverter);
		}
		else {
			return new DefaultOssTemplate(properties, s3Client, s3Presigner, s3TransferManager);
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

}
