package com.hccake.ballcat.common.oss;

import com.hccake.ballcat.common.oss.prefix.ObjectKeyPrefixConverter;
import lombok.Getter;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.FileUpload;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OSS操作模板[对象key带全局前缀]
 *
 * @author lishangbu 2022/10/23
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
	public ObjectWithGlobalKeyPrefixOssTemplate(OssProperties ossProperties,
			ObjectKeyPrefixConverter objectKeyPrefixConverter) {
		super(ossProperties);
		this.objectKeyPrefixConverter = objectKeyPrefixConverter;
	}

	@Override
	public List<S3Object> listObjects(String bucket, String prefix, Integer maxKeys) {
		// 构造API_ListObjects请求
		List<S3Object> contents = s3Client
			.listObjects(ListObjectsRequest.builder()
				.bucket(bucket)
				.maxKeys(maxKeys)
				.prefix(objectKeyPrefixConverter.wrap(prefix))
				.build())
			.contents();
		return objectKeyPrefixConverter.match() ? contents.stream()
			.map(ele -> S3Object.builder()
				.checksumAlgorithm(ele.checksumAlgorithm())
				.checksumAlgorithmWithStrings(ele.checksumAlgorithmAsStrings())
				.eTag(ele.eTag())
				.lastModified(ele.lastModified())
				.key(objectKeyPrefixConverter.unwrap(ele.key()))
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
		return super.putObject(bucket, objectKeyPrefixConverter.wrap(key), file);
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
				.key(objectKeyPrefixConverter.wrap(putObjectRequest.key()))
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
		return super.deleteObject(bucket, objectKeyPrefixConverter.wrap(key));
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
				.key(objectKeyPrefixConverter.wrap(deleteObjectRequest.key()))
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
	 * 获取文件URL,需保证有访问权限
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @return url
	 */
	@Override
	public String getURL(String bucket, String key) {
		return super.getURL(bucket, objectKeyPrefixConverter.wrap(key));
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
		return super.getObjectPresignedUrl(bucket, objectKeyPrefixConverter.wrap(key), duration);
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
		return super.uploadFile(bucket, objectKeyPrefixConverter.wrap(key), file);
	}

}
