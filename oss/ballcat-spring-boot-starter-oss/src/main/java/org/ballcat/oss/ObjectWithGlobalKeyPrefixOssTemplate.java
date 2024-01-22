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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import org.ballcat.oss.prefix.ObjectKeyPrefixConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;

/**
 * OSS操作模板[对象key带全局前缀]
 *
 * @author lishangbu 2022/10/23
 * @author evil0th
 */
public class ObjectWithGlobalKeyPrefixOssTemplate extends DefaultOssTemplate {

	/**
	 * 对象key前缀转换器
	 */
	@Getter
	private final ObjectKeyPrefixConverter objectKeyPrefixConverter;

	/**
	 * 构造器
	 * @param ossProperties OSS属性配置文件
	 * @param objectKeyPrefixConverter 对象全局键前缀转换器
	 */
	public ObjectWithGlobalKeyPrefixOssTemplate(OssProperties ossProperties, S3Client s3Client, S3Presigner s3Presigner,
			S3TransferManager s3TransferManager, ObjectKeyPrefixConverter objectKeyPrefixConverter) {
		super(ossProperties, s3Client, s3Presigner, s3TransferManager);
		this.objectKeyPrefixConverter = objectKeyPrefixConverter;
	}

	@Override
	public List<S3Object> listObjects(String bucket, String prefix, Integer maxKeys) {
		// 构造API_ListObjects请求
		List<S3Object> contents = this.s3Client
			.listObjects(ListObjectsRequest.builder()
				.bucket(bucket)
				.maxKeys(maxKeys)
				.prefix(this.objectKeyPrefixConverter.wrap(prefix))
				.build())
			.contents();
		return this.objectKeyPrefixConverter.match() ? contents.stream()
			.map(ele -> S3Object.builder()
				.checksumAlgorithm(ele.checksumAlgorithm())
				.checksumAlgorithmWithStrings(ele.checksumAlgorithmAsStrings())
				.eTag(ele.eTag())
				.lastModified(ele.lastModified())
				.key(this.objectKeyPrefixConverter.unwrap(ele.key()))
				.owner(ele.owner())
				.size(ele.size())
				.storageClass(ele.storageClass())
				.build())
			.collect(Collectors.toList()) : contents;
	}

	/**
	 * 上传文件
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param file 文件
	 * @return 文件服务器针对上传对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @throws IOException IO异常
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_PutObject.html">往存储桶中添加对象</a>
	 */
	@Override
	public PutObjectResponse putObject(String bucket, String key, File file)
			throws AwsServiceException, SdkClientException, S3Exception, IOException {
		return super.putObject(bucket, this.objectKeyPrefixConverter.wrap(key), file);
	}

	/**
	 * 上传文件
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param sourcePath 文件地址
	 * @return 文件服务器针对上传对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_PutObject.html">往存储桶中添加对象</a>
	 */
	@Override
	public PutObjectResponse putObject(String bucket, String key, Path sourcePath)
			throws AwsServiceException, SdkClientException, S3Exception {
		return super.putObject(bucket, this.objectKeyPrefixConverter.wrap(key), sourcePath);
	}

	/**
	 * 上传文件
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param inputStream 文件输入流
	 * @param contentLength 文件大小
	 * @return 文件服务器针对上传对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_PutObject.html">往存储桶中添加对象</a>
	 */
	@Override
	public PutObjectResponse putObject(String bucket, String key, InputStream inputStream, long contentLength)
			throws AwsServiceException, SdkClientException, S3Exception {
		return super.putObject(bucket, this.objectKeyPrefixConverter.wrap(key), inputStream, contentLength);
	}

	/**
	 * 上传文件
	 * @param putObjectRequest PutObjectRequest
	 * @param requestBody RequestBody
	 * @return 文件服务器针对上传对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_PutObject.html">往存储桶中添加对象</a>
	 */
	@Override
	public PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody)
			throws AwsServiceException, SdkClientException, S3Exception {
		if (StringUtils.hasText(putObjectRequest.key())) {
			return super.putObject(PutObjectRequest.builder()
				.acl(putObjectRequest.acl())
				.contentType(putObjectRequest.contentType())
				.key(this.objectKeyPrefixConverter.wrap(putObjectRequest.key()))
				.bucket(putObjectRequest.bucket())
				.contentLength(putObjectRequest.contentLength())
				.cacheControl(putObjectRequest.cacheControl())
				.metadata(putObjectRequest.metadata())
				.checksumAlgorithm(putObjectRequest.checksumAlgorithm())
				.checksumCRC32(putObjectRequest.checksumCRC32())
				.checksumCRC32C(putObjectRequest.checksumCRC32C())
				.checksumSHA1(putObjectRequest.checksumSHA1())
				.checksumSHA256(putObjectRequest.checksumSHA256())
				.bucketKeyEnabled(putObjectRequest.bucketKeyEnabled())
				.contentEncoding(putObjectRequest.contentEncoding())
				.contentMD5(putObjectRequest.contentMD5())
				.websiteRedirectLocation(putObjectRequest.websiteRedirectLocation())
				.expectedBucketOwner(putObjectRequest.expectedBucketOwner())
				.expires(putObjectRequest.expires())
				.grantFullControl(putObjectRequest.grantFullControl())
				.grantRead(putObjectRequest.grantRead())
				.grantReadACP(putObjectRequest.grantReadACP())
				.grantWriteACP(putObjectRequest.grantWriteACP())
				.contentLanguage(putObjectRequest.contentLanguage())
				.objectLockMode(putObjectRequest.objectLockMode())
				.objectLockLegalHoldStatus(putObjectRequest.objectLockLegalHoldStatus())
				.objectLockRetainUntilDate(putObjectRequest.objectLockRetainUntilDate())
				.overrideConfiguration(putObjectRequest.overrideConfiguration().orElse(null))
				.requestPayer(putObjectRequest.requestPayer())
				.serverSideEncryption(putObjectRequest.serverSideEncryption())
				.sseCustomerAlgorithm(putObjectRequest.sseCustomerAlgorithm())
				.sseCustomerKey(putObjectRequest.sseCustomerKey())
				.sseCustomerKeyMD5(putObjectRequest.sseCustomerKeyMD5())
				.ssekmsEncryptionContext(putObjectRequest.ssekmsEncryptionContext())
				.ssekmsKeyId(putObjectRequest.ssekmsKeyId())
				.contentDisposition(putObjectRequest.contentDisposition())
				.tagging(putObjectRequest.tagging())
				.build(), requestBody);
		}
		return super.putObject(putObjectRequest, requestBody);
	}

	/**
	 * 删除对象
	 * @param bucket 存储桶
	 * @param key 对象键,支持静默全局前缀键操作
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObject.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectResponse deleteObject(String bucket, String key) {
		return super.deleteObject(bucket, this.objectKeyPrefixConverter.wrap(key));
	}

	/**
	 * 删除多个对象
	 * @param bucket 存储桶
	 * @param keys 对象键,支持静默全局前缀键操作
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObjects.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectsResponse deleteObjects(String bucket, Set<String> keys) {
		return super.deleteObjects(bucket,
				keys.stream().map(this.objectKeyPrefixConverter::wrap).collect(Collectors.toSet()));
	}

	/**
	 * 删除对象
	 * @param deleteObjectRequest 通用删除对象请求,如果包含key，则对key进行包装
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObject.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectResponse deleteObject(DeleteObjectRequest deleteObjectRequest) {

		if (StringUtils.hasText(deleteObjectRequest.key())) {
			return super.deleteObject(DeleteObjectRequest.builder()
				.bucket(deleteObjectRequest.bucket())
				.key(this.objectKeyPrefixConverter.wrap(deleteObjectRequest.key()))
				.bypassGovernanceRetention(deleteObjectRequest.bypassGovernanceRetention())
				.expectedBucketOwner(deleteObjectRequest.expectedBucketOwner())
				.mfa(deleteObjectRequest.mfa())
				.overrideConfiguration(deleteObjectRequest.overrideConfiguration().orElse(null))
				.requestPayer(deleteObjectRequest.requestPayer())
				.versionId(deleteObjectRequest.versionId())
				.build());
		}
		else {
			return super.deleteObject(deleteObjectRequest);
		}
	}

	/**
	 * 删除多个对象
	 * @param deleteObjectsRequest 通用删除对象请求
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObjects.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectsResponse deleteObjects(DeleteObjectsRequest deleteObjectsRequest) {
		if (!CollectionUtils.isEmpty(deleteObjectsRequest.delete().objects())) {
			List<ObjectIdentifier> toDelete = deleteObjectsRequest.delete()
				.objects()
				.stream()
				.map(e -> ObjectIdentifier.builder().key(this.objectKeyPrefixConverter.wrap(e.key())).build())
				.collect(Collectors.toList());
			return super.deleteObjects(DeleteObjectsRequest.builder()
				.bucket(deleteObjectsRequest.bucket())
				.delete(Delete.builder().objects(toDelete).build())
				.bypassGovernanceRetention(deleteObjectsRequest.bypassGovernanceRetention())
				.expectedBucketOwner(deleteObjectsRequest.expectedBucketOwner())
				.mfa(deleteObjectsRequest.mfa())
				.overrideConfiguration(deleteObjectsRequest.overrideConfiguration().orElse(null))
				.requestPayer(deleteObjectsRequest.requestPayer())
				.build());
		}
		else {
			return super.deleteObjects(deleteObjectsRequest);
		}
	}

	/**
	 * 复制对象
	 * @param sourceKey 原对象键
	 * @param destinationKey 目标对象键,支持静默全局前缀键操作
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_CopyObject.html">从存储桶中删除对象</a>
	 */
	@Override
	public CopyObjectResponse copyObject(String bucket, String sourceKey, String destinationBucket,
			String destinationKey) {
		return super.copyObject(bucket, this.objectKeyPrefixConverter.wrap(sourceKey), destinationBucket,
				this.objectKeyPrefixConverter.wrap(destinationKey));
	}

	/**
	 * 获取文件URL,需保证有访问权限
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @return url
	 */
	@Override
	public String getURL(String bucket, String key) {
		return super.getURL(bucket, this.objectKeyPrefixConverter.wrap(key));
	}

	/**
	 * 获取文件外链
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param duration 过期时间
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/GetObjectPresignedUrl.java">获取文件外链</a>
	 */
	@Override
	public String getObjectPresignedUrl(String bucket, String key, Duration duration) {
		return super.getObjectPresignedUrl(bucket, this.objectKeyPrefixConverter.wrap(key), duration);
	}

	/**
	 * 获取文件预签名外链（上传）
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param duration 过期时间
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	@Override
	public String putObjectPresignedUrl(String bucket, String key, Duration duration) {
		return super.putObjectPresignedUrl(bucket, this.objectKeyPrefixConverter.wrap(key), duration);
	}

	/**
	 * Schedules a new transfer to upload data to Amazon S3. This method is non-blocking
	 * and returns immediately (i.e. before the upload has finished).
	 * <p>
	 * The returned Upload object allows you to query the progress of the transfer, add
	 * listeners for progress events, and wait for the upload to complete.
	 * <p>
	 * If resources are available, the upload will begin immediately, otherwise it will be
	 * scheduled and started as soon as resources become available.
	 * <p>
	 * If you are uploading <a href="http://aws.amazon.com/kms/">AWS KMS</a>-encrypted
	 * objects, you need to specify the correct region of the bucket on your client and
	 * configure AWS Signature Version 4 for added security. For more information on how
	 * to do this, see http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html#
	 * specify-signature-version
	 * </p>
	 * @param bucket The name of the bucket to upload the new object to.
	 * @param key The key in the specified bucket by which to store the new object.
	 * @param file The file to upload.
	 * @return A new Upload object which can be used to check state of the upload, listen
	 * for progress notifications, and otherwise manage the upload.
	 */
	@Override
	public FileUpload uploadFile(String bucket, String key, File file) {
		return super.uploadFile(bucket, this.objectKeyPrefixConverter.wrap(key), file);
	}

}
