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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

/**
 * OSS操作
 *
 * @author lishangbu
 * @author evil0th
 */
public interface OssTemplate {

	/**
	 * 获取OSS属性配置
	 * @return OSS属性配置
	 */
	@NonNull
	OssProperties getOssProperties();

	/**
	 * 获取S3客户端
	 */
	@NonNull
	S3Client getS3Client();

	/**
	 * 获取S3预签名工具
	 */
	@NonNull
	S3Presigner getS3Presigner();

	/**
	 * 获取S3传输管理器
	 */
	@NonNull
	S3TransferManager getS3TransferManager();

	// region 存储桶相关操作

	/**
	 * 创建bucket
	 * @param createBucketRequest 通用创建bucket请求
	 * @return 文件服务器返回的创建存储桶的响应结果
	 * @throws BucketAlreadyExistsException 请求的存储桶名称不可用。存储桶名称空间由系统的所有用户共享。请选择其他名称然后重试。
	 * @throws BucketAlreadyOwnedByYouException 您尝试创建的存储桶已经存在，并且您拥有它。 Amazon
	 * S3在除北弗吉尼亚州以外的所有AWS地区均返回此错误。为了实现旧兼容性，如果您重新创建在北弗吉尼亚州已经拥有的现有存储桶， Amazon S3将返回200
	 * OK并重置存储桶访问控制列表（ACL）
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_CreateBucket.html">创建存储桶</a>
	 */
	default CreateBucketResponse createBucket(CreateBucketRequest createBucketRequest)
			throws BucketAlreadyExistsException, BucketAlreadyOwnedByYouException, AwsServiceException,
			SdkClientException, S3Exception {
		return getS3Client().createBucket(createBucketRequest);
	}

	/**
	 * 创建bucket
	 * @param bucket 存储桶名称
	 * @return 文件服务器返回的创建存储桶的响应结果
	 * @throws BucketAlreadyExistsException 请求的存储桶名称不可用。存储桶名称空间由系统的所有用户共享。请选择其他名称然后重试。
	 * @throws BucketAlreadyOwnedByYouException 您尝试创建的存储桶已经存在，并且您拥有它。 Amazon
	 * S3在除北弗吉尼亚州以外的所有AWS地区均返回此错误。为了实现旧兼容性，如果您重新创建在北弗吉尼亚州已经拥有的现有存储桶， Amazon S3将返回200
	 * OK并重置存储桶访问控制列表（ACL）
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_CreateBucket.html">创建存储桶</a>
	 */
	default CreateBucketResponse createBucket(String bucket) throws BucketAlreadyExistsException,
			BucketAlreadyOwnedByYouException, AwsServiceException, SdkClientException, S3Exception {
		return createBucket(CreateBucketRequest.builder().bucket(bucket).build());
	}

	/**
	 * 创建bucket
	 * @param bucket 存储桶名称
	 * @return 文件服务器返回的创建存储桶的响应结果
	 * @throws BucketAlreadyExistsException 请求的存储桶名称不可用。存储桶名称空间由系统的所有用户共享。请选择其他名称然后重试。
	 * @throws BucketAlreadyOwnedByYouException 您尝试创建的存储桶已经存在，并且您拥有它。 Amazon
	 * S3在除北弗吉尼亚州以外的所有AWS地区均返回此错误。为了实现旧兼容性，如果您重新创建在北弗吉尼亚州已经拥有的现有存储桶， Amazon S3将返回200
	 * OK并重置存储桶访问控制列表（ACL）
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_CreateBucket.html">创建存储桶</a>
	 */
	default CreateBucketResponse createBucket(String bucket, Region region) throws BucketAlreadyExistsException,
			BucketAlreadyOwnedByYouException, AwsServiceException, SdkClientException, S3Exception {
		return createBucket(bucket, region.id());
	}

	/**
	 * 创建bucket
	 * @param bucket 存储桶名称
	 * @param region 区域
	 * @return 文件服务器返回的创建存储桶的响应结果
	 * @throws BucketAlreadyExistsException 请求的存储桶名称不可用。存储桶名称空间由系统的所有用户共享。请选择其他名称然后重试。
	 * @throws BucketAlreadyOwnedByYouException 您尝试创建的存储桶已经存在，并且您拥有它。 Amazon
	 * S3在除北弗吉尼亚州以外的所有AWS地区均返回此错误。为了实现旧兼容性，如果您重新创建在北弗吉尼亚州已经拥有的现有存储桶， Amazon S3将返回200
	 * OK并重置存储桶访问控制列表（ACL）
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_CreateBucket.html">创建存储桶</a>
	 */
	default CreateBucketResponse createBucket(String bucket, String region) throws BucketAlreadyExistsException,
			BucketAlreadyOwnedByYouException, AwsServiceException, SdkClientException, S3Exception {
		return createBucket(CreateBucketRequest.builder()
			.createBucketConfiguration(CreateBucketConfiguration.builder().locationConstraint(region).build())
			.bucket(bucket)
			.build());
	}

	/**
	 * @param listBucketsRequest 罗列存储桶请求 获取当前认证用户持有的全部存储桶信息列表
	 * @return 当前认证用户持有的获取全部存储桶信息列表
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_ListBuckets.html">罗列存储桶</a>
	 */
	default ListBucketsResponse listBuckets(ListBucketsRequest listBucketsRequest)
			throws AwsServiceException, SdkClientException, S3Exception {
		return getS3Client().listBuckets(listBucketsRequest);
	}

	/**
	 * 获取当前认证用户持有的全部存储桶信息列表
	 * @return 当前认证用户持有的获取全部存储桶信息列表
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_ListBuckets.html">罗列存储桶</a>
	 */
	default ListBucketsResponse listBuckets() throws AwsServiceException, SdkClientException, S3Exception {
		return getS3Client().listBuckets();
	}

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
	default DeleteBucketResponse deleteBucket(DeleteBucketRequest deleteBucketRequest) {
		return getS3Client().deleteBucket(deleteBucketRequest);
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
	default DeleteBucketResponse deleteBucket(String bucket) {
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
	default List<S3Object> listObjects(String prefix) {
		return listObjects(getOssProperties().getBucket(), prefix);
	}

	/**
	 * 根据文件前缀查询对象列表
	 * @param bucket 存储桶名称
	 * @param prefix 对象前缀
	 * @return 对象列表
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_ListObjects.html">罗列对象</a>
	 */
	default List<S3Object> listObjects(String bucket, String prefix) {
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
	List<S3Object> listObjects(String bucket, String prefix, Integer maxKeys);

	/**
	 * 上传文件
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param sourcePath 文件地址
	 * @return 文件服务器针对上传对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @throws IOException IO异常
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_PutObject.html">往存储桶中添加对象</a>
	 */
	PutObjectResponse putObject(String bucket, String key, Path sourcePath)
			throws AwsServiceException, SdkClientException, S3Exception, IOException;

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
	PutObjectResponse putObject(String bucket, String key, File file)
			throws AwsServiceException, SdkClientException, S3Exception, IOException;

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
	PutObjectResponse putObject(String bucket, String key, InputStream inputStream, long contentLength)
			throws AwsServiceException, SdkClientException, S3Exception;

	/**
	 * 上传文件
	 * @param putObjectRequest PutObjectRequest对象
	 * @param requestBody RequestBody对象
	 * @return 文件服务器针对上传对象操作的返回结果
	 * @throws AwsServiceException SDK可能引发的所有异常的基类（不论是服务端异常还是客户端异常）。可用于所有场景下的异常捕获。
	 * @throws SdkClientException 如果发生任何客户端错误，例如与IO相关的异常，无法获取凭据等,会抛出此异常
	 * @throws S3Exception 所有服务端异常的基类。未知异常将作为此类型的实例抛出
	 * @see <a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/API/API_PutObject.html">往存储桶中添加对象</a>
	 */
	default PutObjectResponse putObject(PutObjectRequest putObjectRequest, RequestBody requestBody)
			throws AwsServiceException, SdkClientException, S3Exception {
		return getS3Client().putObject(putObjectRequest, requestBody);
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
	default DeleteObjectResponse deleteObject(DeleteObjectRequest deleteObjectRequest) {
		return getS3Client().deleteObject(deleteObjectRequest);
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
	default DeleteObjectsResponse deleteObjects(DeleteObjectsRequest deleteObjectsRequest) {
		return getS3Client().deleteObjects(deleteObjectsRequest);
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
	default DeleteObjectResponse deleteObject(String key) {
		return deleteObject(getOssProperties().getBucket(), key);
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
	default DeleteObjectsResponse deleteObjects(Set<String> keys) {
		return deleteObjects(getOssProperties().getBucket(), keys);
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
	default DeleteObjectResponse deleteObject(String bucket, String key) {
		return getS3Client().deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
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
	default DeleteObjectsResponse deleteObjects(String bucket, Set<String> keys) {
		List<ObjectIdentifier> toDelete = keys.stream()
			.map(e -> ObjectIdentifier.builder().key(e).build())
			.collect(Collectors.toList());
		return getS3Client().deleteObjects(DeleteObjectsRequest.builder()
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
	default CopyObjectResponse copyObject(String sourceKey, String destinationKey) {
		return copyObject(getOssProperties().getBucket(), sourceKey, destinationKey);
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
	default CopyObjectResponse copyObject(String bucket, String sourceKey, String destinationKey) {
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
	default CopyObjectResponse copyObject(String sourceBucket, String sourceKey, String destinationBucket,
			String destinationKey) {
		return getS3Client().copyObject(CopyObjectRequest.builder()
			.sourceBucket(sourceBucket)
			.sourceKey(sourceKey)
			.destinationBucket(destinationBucket)
			.destinationKey(destinationKey)
			.build());
	}

	/**
	 * 获取文件URL,需保证有访问权限
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @return url
	 */
	default String getURL(String bucket, String key) {
		S3Utilities utilities = getS3Client().utilities();
		String url = utilities.getUrl(GetUrlRequest.builder().key(key).bucket(bucket).build()).toString();
		String domain = getOssProperties().getDomain();
		return StringUtils.hasText(domain) ? url.replace(getOssProperties().getEndpoint(), domain) : url;
	}

	/**
	 * 获取文件URL,需保证有访问权限
	 * @param key 文件名称
	 * @return url
	 */
	default String getURL(String key) {
		return getURL(getOssProperties().getBucket(), key);
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
	String getObjectPresignedUrl(String bucket, String key, Duration duration);

	/**
	 * 获取文件预签名外链(上传)
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param duration 过期时间
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	String putObjectPresignedUrl(String bucket, String key, Duration duration);

	/**
	 * 获取文件外链,默认2小时后过期
	 * @param bucket bucket名称
	 * @param keyName 文件名称
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/GetObjectPresignedUrl.java">获取文件外链</a>
	 */
	default String getObjectPresignedUrl(String bucket, String keyName) {
		return getObjectPresignedUrl(bucket, keyName, Duration.ofHours(2));
	}

	/**
	 * 获取文件外链,默认2小时后过期
	 * @param keyName 文件名称
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/s3/src/main/java/com/example/s3/GetObjectPresignedUrl.java">获取文件外链</a>
	 */
	default String getObjectPresignedUrl(String keyName) {
		return getObjectPresignedUrl(getOssProperties().getBucket(), keyName, Duration.ofHours(2));
	}

	/**
	 * 获取文件预签名外链(上传),默认2小时后过期
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	default String putObjectPresignedUrl(String bucket, String key) {
		return putObjectPresignedUrl(bucket, key, Duration.ofMinutes(2));
	}

	/**
	 * 获取文件预签名外链(上传),默认2小时后过期
	 * @param key 文件名称
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	default String putObjectPresignedUrl(String key) {
		return putObjectPresignedUrl(getOssProperties().getBucket(), key);
	}

	/**
	 * 获取文件预签名外链(上传,不含域名+bucket部分用于前端)
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @param duration 过期时间
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	default String putObjectPresignedUri(String bucket, String key, Duration duration) {
		String url = putObjectPresignedUrl(bucket, key, duration);
		return url.substring(url.indexOf(key));
	}

	/**
	 * 获取文件预签名外链(上传,不含域名+bucket部分用于前端),默认2小时后过期
	 * @param bucket bucket名称
	 * @param key 文件名称
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	default String putObjectPresignedUri(String bucket, String key) {
		return putObjectPresignedUri(bucket, key, Duration.ofMinutes(2));
	}

	/**
	 * 获取文件预签名外链(上传,不含域名+bucket部分用于前端),默认2小时后过期
	 * @param key 文件名称
	 * @return url的文本表示
	 * @see <a href=
	 * "https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/s3/src/main/java/com/example/s3/GeneratePresignedUrlAndUploadObject.java">获取文件预授权外链</a>
	 */
	default String putObjectPresignedUri(String key) {
		return putObjectPresignedUri(getOssProperties().getBucket(), key);
	}

	// endregion

	// region 用于上传的高级API

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
	default FileUpload uploadFile(String bucket, String key, File file) {
		return getS3TransferManager().uploadFile(UploadFileRequest.builder()
			.putObjectRequest(PutObjectRequest.builder().bucket(bucket).key(key).build())
			.source(file)
			.build());
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
	 * @param key The key in the specified bucket by which to store the new object.
	 * @param file The file to upload.
	 * @return A new Upload object which can be used to check state of the upload, listen
	 * for progress notifications, and otherwise manage the upload.
	 */
	default FileUpload uploadFile(String key, File file) {
		return uploadFile(getOssProperties().getBucket(), key, file);
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
	 * @param file The file to upload.
	 * @return A new Upload object which can be used to check state of the upload, listen
	 * for progress notifications, and otherwise manage the upload.
	 */
	default FileUpload uploadFile(File file) {
		return uploadFile(getOssProperties().getBucket(), file.getName(), file);
	}

	/**
	 * 上传一个本地文件到S3中的对象。对于非基于文件的上传，您可以使用 {@link #upload(UploadRequest)} 替代.
	 * <p>
	 * <b>使用示例:</b> <pre>
	 * {@code
	 * FileUpload upload =
	 *     tm.uploadFile(u -> u.source(Paths.get("myFile.txt"))
	 *                         .putObjectRequest(p -> p.bucket("bucket").key("key")));
	 * // 等待传输完成
	 * upload.completionFuture().join();
	 * }
	 * </pre>
	 *
	 * @see #uploadFile(Consumer)
	 * @see #upload(UploadRequest)
	 */
	FileUpload uploadFile(UploadFileRequest uploadFileRequest);

	/**
	 * 这是一个简便的方法用于创建一个 {@link UploadFileRequest} 的构建器, 避免了手动创建
	 * {@link UploadFileRequest#builder()}.
	 *
	 * @see #uploadFile(UploadFileRequest)
	 */
	default FileUpload uploadFile(Consumer<UploadFileRequest.Builder> request) {
		return uploadFile(UploadFileRequest.builder().applyMutation(request).build());
	}

	/**
	 * Upload the given {@link AsyncRequestBody} to an object in S3. For file-based
	 * uploads, you may use {@link #uploadFile(UploadFileRequest)} instead.
	 * <p>
	 * <b>Usage Example:</b> <pre>
	 * {@code
	 * Upload upload =
	 *     tm.upload(u -> u.requestBody(AsyncRequestBody.fromString("Hello world"))
	 *                     .putObjectRequest(p -> p.bucket("bucket").key("key")));
	 * // Wait for the transfer to complete
	 * upload.completionFuture().join();
	 * }
	 * </pre> See the static factory methods available in {@link AsyncRequestBody} for
	 * other use cases.
	 * @param uploadRequest the upload request, containing a {@link PutObjectRequest} and
	 * {@link AsyncRequestBody}
	 * @return An {@link Upload} that can be used to track the ongoing transfer
	 * @see #upload(Consumer)
	 * @see #uploadFile(UploadFileRequest)
	 */
	default Upload upload(UploadRequest uploadRequest) {
		return getS3TransferManager().upload(uploadRequest);
	}

	/**
	 * This is a convenience method that creates an instance of the {@link UploadRequest}
	 * builder, avoiding the need to create one manually via
	 * {@link UploadRequest#builder()}.
	 *
	 * @see #upload(UploadRequest)
	 */
	default Upload upload(Consumer<UploadRequest.Builder> request) {
		return upload(UploadRequest.builder().applyMutation(request).build());
	}

	// endregion

}
