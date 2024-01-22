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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.transfer.s3.model.FileUpload;

/**
 * MINIO OSS测试工具
 *
 * @author lishangbu 2022/10/22
 */

@ActiveProfiles("minio")
@RequiredArgsConstructor
class MinioOssTemplateTest extends AbstractOssTemplateTest {

	/**
	 * 测试用OSS名字
	 */
	private static final String TEST_BUCKET_NAME = "test";

	/**
	 * 测试用上传后的文件名
	 */
	private static final String TEST_OBJECT_NAME = "b.txt";

	/**
	 * 测试创建存储桶
	 */
	@Test
	void createBucket() {
		String bucket = UUID.randomUUID().toString();
		CreateBucketResponse bucketResponse = this.ossTemplate.createBucket(bucket);
		Assertions.assertTrue(bucketResponse.location().endsWith(bucket));
		deleteBucket(bucket);
	}

	/**
	 * 测试罗列存储桶
	 */
	@Test
	void listBuckets() {
		String bucket = UUID.randomUUID().toString();
		createBucket(bucket);
		List<Bucket> newBuckets = this.ossTemplate.listBuckets().buckets();
		List<String> bucketNames = newBuckets.stream().map(Bucket::name).collect(Collectors.toList());
		Assertions.assertTrue(bucketNames.contains(bucket));
		deleteBucket(bucket);
	}

	/**
	 * 测试删除存储桶
	 */
	@Test
	void deleteBucket() {
		this.ossTemplate.deleteBucket(TEST_BUCKET_NAME);
	}

	@Test
	void getObjectPresignedUrl() {
		String objectPresignedUrl = this.ossTemplate.getObjectPresignedUrl(
				this.ossTemplate.getOssProperties().getBucket(), TEST_OBJECT_NAME, Duration.ofDays(1));
		System.out.println(objectPresignedUrl);
	}

	@Test
	void getUrl() {
		String url = this.ossTemplate.getURL(this.ossTemplate.getOssProperties().getBucket(), TEST_OBJECT_NAME);
		System.out.println(url);
	}

	@Test
	void getUrlWithCustomPrefix() {
		URL url = this.ossTemplate.getS3Client()
			.utilities()
			.getUrl(GetUrlRequest.builder()
				.bucket(this.ossTemplate.getOssProperties().getBucket())
				.key(TEST_OBJECT_NAME)
				.endpoint(URI.create(this.ossTemplate.getOssProperties().getEndpoint() + "/测试/"))
				.region(Region.of(this.ossTemplate.getOssProperties().getRegion()))
				.build());

		System.out.println(url);
	}

	/**
	 * 测试获取对象
	 */
	@Test
	void listObjects() {
		// 使用上传时自己构建的的对象名字查询
		List<S3Object> s3ObjectsWithUploadKey = this.ossTemplate
			.listObjects(this.ossTemplate.getOssProperties().getBucket(), TEST_OBJECT_NAME);
		Assertions.assertEquals(1, s3ObjectsWithUploadKey.size());
		Assertions.assertEquals(TEST_OBJECT_NAME, s3ObjectsWithUploadKey.get(0).key());
	}

	/**
	 * 测试文件上传 上传后的文件名和当前文件名保持一致
	 */
	@Test
	void putObject() throws IOException {
		PutObjectResponse putObjectResponse = this.ossTemplate.putObject(
				this.ossTemplate.getOssProperties().getBucket(), TEST_OBJECT_NAME,
				ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "test.txt"));
		System.out.println(putObjectResponse);

	}

	@Test
	void upload() throws FileNotFoundException {
		String bucket = UUID.randomUUID().toString();
		String key = UUID.randomUUID().toString();
		createBucket(bucket);
		FileUpload fileUpload = this.ossTemplate.uploadFile(key,
				ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "test.txt"));
		fileUpload.completionFuture().join();
		List<S3Object> s3Objects = this.ossTemplate.listObjects(key);
		Assertions.assertEquals(1, s3Objects.size());
		S3Object s3Object = s3Objects.get(0);
		Assertions.assertEquals(key, s3Object.key());
		Assertions.assertEquals(13, s3Object.size());
		this.ossTemplate.deleteObject(key);
		deleteBucket(bucket);
	}

	@Test
	void testBean() {
		if (this.objectKeyPrefixConverter.match()) {
			Assertions.assertInstanceOf(ObjectWithGlobalKeyPrefixOssTemplate.class, this.ossTemplate);
		}
		else {
			Assertions.assertInstanceOf(DefaultOssTemplate.class, this.ossTemplate);
		}
	}

}
