package com.hccake.ballcat.common.oss;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OSS操作模板
 * <p>
 * 低级API实现
 * </p>
 * <p>
 * 兼容1.x com.amazonaws.services.s3.AbstractAmazonS3部分API
 * </p>
 *
 * @author lishangbu
 * @author evil0th
 */
@RequiredArgsConstructor
public class DefaultOssTemplate implements OssTemplate {

	/**
	 * 对象存储服务配置
	 */
	@Getter
	protected final OssProperties ossProperties;

	/**
	 * S3客户端
	 */
	@Getter
	protected final S3Client s3Client;

	/**
	 * S3预签名工具
	 */
	@Getter
	protected final S3Presigner s3Presigner;

	@Getter
	protected final S3TransferManager s3TransferManager;

	// region 存储桶相关操作

	/**
	 * 删除存储桶
	 * @param deleteBucketRequest 删除存储桶请求
	 * @return 文件服务器返回的删除存储桶的响应结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteBucket.html">删除存储桶</a>
	 */
	@Override
	public DeleteBucketResponse deleteBucket(DeleteBucketRequest deleteBucketRequest) {
		return s3Client.deleteBucket(deleteBucketRequest);
	}

	/**
	 * 删除存储桶
	 * @param bucket 待删除的存储桶名称
	 * @return 文件服务器返回的删除存储桶的响应结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteBucket.html">删除存储桶</a>
	 */
	@Override
	public DeleteBucketResponse deleteBucket(String bucket) {
		return deleteBucket(DeleteBucketRequest.builder().bucket(bucket).build());
	}

	// endregion

	// region 对象相关操作

	/**
	 * 根据文件前缀查询对象列表
	 * @param prefix 对象前缀
	 * @return 对象列表
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_ListObjects.html">罗列对象</a>
	 */
	@Override
	public List<S3Object> listObjects(String prefix) {
		return listObjects(ossProperties.getBucket(), prefix);
	}

	/**
	 * 根据文件前缀查询对象列表
	 * @param bucket 存储桶名称
	 * @param prefix 对象前缀
	 * @return 对象列表
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_ListObjects.html">罗列对象</a>
	 */
	@Override
	public List<S3Object> listObjects(String bucket, String prefix) {
		return listObjects(bucket, prefix, 1000);
	}

	/**
	 * 根据文件前缀查询对象列表
	 * @param bucket 存储桶名称
	 * @param prefix 对象前缀
	 * @param maxKeys 设置在响应中返回的键的最大数量。默认情况下，该操作最多返回1,000个键名。响应可能包含更少的键，但永远不会包含更多
	 * @return 对象列表
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_ListObjects.html">罗列对象</a>
	 */
	@Override
	public List<S3Object> listObjects(String bucket, String prefix, Integer maxKeys) {
		return s3Client.listObjects(ListObjectsRequest.builder().maxKeys(maxKeys).prefix(prefix).bucket(bucket).build())
			.contents();
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
		return s3Client.putObject(PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.contentLength(file.length())
			.contentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file))
			.build(), RequestBody.fromFile(file));
	}

	/**
	 * 删除对象
	 * @param deleteObjectRequest 通用删除对象请求
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObject.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectResponse deleteObject(DeleteObjectRequest deleteObjectRequest) {
		return s3Client.deleteObject(deleteObjectRequest);
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
		return s3Client.deleteObjects(deleteObjectsRequest);
	}

	/**
	 * 删除对象
	 * @param key 对象键
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObject.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectResponse deleteObject(String key) {
		return deleteObject(ossProperties.getBucket(), key);
	}

	/**
	 * 删除多个对象
	 * @param keys 对象键
	 * @return 文件服务器针对删除对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_DeleteObjects.html">从存储桶中删除对象</a>
	 */
	@Override
	public DeleteObjectsResponse deleteObjects(Set<String> keys) {
		return deleteObjects(ossProperties.getBucket(), keys);
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
		return deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
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
		List<ObjectIdentifier> toDelete = keys.stream()
			.map(e -> ObjectIdentifier.builder().key(e).build())
			.collect(Collectors.toList());
		return deleteObjects(DeleteObjectsRequest.builder()
			.bucket(bucket)
			.delete(Delete.builder().objects(toDelete).build())
			.build());
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
	public CopyObjectResponse copyObject(String sourceKey, String destinationKey) {
		return copyObject(ossProperties.getBucket(), sourceKey, destinationKey);
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
	public CopyObjectResponse copyObject(String bucket, String sourceKey, String destinationKey) {
		return copyObject(bucket, sourceKey, bucket, destinationKey);
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
	public CopyObjectResponse copyObject(String sourceBucket, String sourceKey, String destinationBucket,
			String destinationKey) {
		return s3Client.copyObject(CopyObjectRequest.builder()
			.sourceBucket(sourceBucket)
			.sourceKey(sourceKey)
			.destinationBucket(destinationBucket)
			.destinationKey(destinationKey)
			.build());
	}

	@Override
	public FileUpload uploadFile(UploadFileRequest uploadFileRequest) {
		return this.s3TransferManager.uploadFile(uploadFileRequest);
	}

	@Override
	public Upload upload(UploadRequest uploadRequest) {
		return this.s3TransferManager.upload(uploadRequest);
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

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();

		GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(duration)
			.getObjectRequest(getObjectRequest)
			.build();

		PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
		URL url = presignedGetObjectRequest.url();
		return url.toString();
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
		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key).build();

		PutObjectPresignRequest getObjectPresignRequest = PutObjectPresignRequest.builder()
			.signatureDuration(duration)
			.putObjectRequest(putObjectRequest)
			.build();

		PresignedPutObjectRequest presignedGetObjectRequest = s3Presigner.presignPutObject(getObjectPresignRequest);
		URL url = presignedGetObjectRequest.url();
		return url.toString();
	}

	// endregion

}
