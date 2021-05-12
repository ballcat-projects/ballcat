package com.hccake.ballcat.commom.oss;

import com.hccake.ballcat.commom.oss.domain.StreamTemp;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.hccake.ballcat.commom.oss.OssConstants.*;

/**
 * @author lingting 2021/5/11 9:59
 */
@Getter
public class OssClient implements DisposableBean {

	private final String endpoint;

	private final String accessKey;

	private final String accessSecret;

	private final String bucket;

	private final String root;

	private final S3Client client;

	private final ObjectCannedACL acl;

	private String downloadPrefix;

	@SneakyThrows
	public OssClient(String endpoint, String accessKey, String accessSecret, String bucket, String root,
			ObjectCannedACL acl) {
		this.endpoint = endpoint;
		this.accessKey = accessKey;
		this.accessSecret = accessSecret;
		this.bucket = bucket;
		this.root = root;
		this.acl = acl;
		final S3ClientBuilder builder = S3Client.builder();
		// 地区
		region(endpoint, bucket, builder);

		// 不以 / 结尾
		if (downloadPrefix.endsWith(SLASH)) {
			downloadPrefix = downloadPrefix.substring(0, downloadPrefix.length() - 1);
		}

		// 配置
		configuration(bucket, builder);

		client = builder
				// key secret
				.credentialsProvider(
						StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, accessSecret)))
				.build();
	}

	private void configuration(String bucket, S3ClientBuilder builder) {
		builder.overrideConfiguration(cb -> cb.addExecutionInterceptor(new ExecutionInterceptor() {
			@SneakyThrows
			@Override
			public SdkHttpRequest modifyHttpRequest(Context.ModifyHttpRequest context,
					ExecutionAttributes executionAttributes) {

				SdkHttpRequest request = context.httpRequest();
				final SdkHttpRequest.Builder rb = SdkHttpRequest.builder()

						.protocol(request.protocol())

						.port(request.port())

						.headers(request.headers())

						.method(request.method())

						.rawQueryParameters(request.rawQueryParameters());

				// 根据文档
				// https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/VirtualHosting.html
				// 把 host 改为 bucket.region... 形式
				if (!request.host().startsWith(bucket)) {
					rb.host(bucket + DOT + request.host());
				}
				else {
					rb.host(request.host());
				}

				// host 修改后, 需要移除 path 前的 bucket 声明
				if (request.encodedPath().startsWith(SLASH + bucket)) {
					rb.encodedPath(request.encodedPath().substring((SLASH + bucket).length()));
				}
				else {
					rb.encodedPath(request.encodedPath());
				}

				return rb.build();
			}
		}));
	}

	private void region(String endpoint, String bucket, S3ClientBuilder builder) throws URISyntaxException {
		// 判断传入的是 地区还是节点
		if (endpoint.contains(DOT)) {
			// 节点

			if (endpoint.startsWith(OssConstants.S3)) {
				// 以 s3. 开头
				endpoint = endpoint.substring(OssConstants.S3.length());
			}

			// 从节点中获取地址
			builder.region(Region.of(endpoint.substring(0, endpoint.indexOf(DOT))));

			if (!endpoint.contains(AWS_INTERNATIONAL)) {
				// 非亚马逊节点
				URI uri = new URI(String.format("https://%s.%s", bucket, endpoint));
				// 覆盖节点
				builder.endpointOverride(uri);
				downloadPrefix = uri.toString();
			}
			else {
				// 亚马逊节点
				downloadPrefix = String.format("https://%s.s3.%s", bucket, endpoint);
			}
		}
		else {
			// 地区
			builder.region(Region.of(endpoint));
			// 默认国际版下载地址
			downloadPrefix = String.format("https://%s.s3.%s.%s", bucket, endpoint, AWS_INTERNATIONAL);
		}
	}

	/**
	 * 文件上传, 本方法会读一遍流, 计算流大小, 推荐使用 upload(stream, absolutePath, size) 方法
	 * @param absolutePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @throws IOException 流操作时异常
	 */
	public String upload(InputStream stream, String absolutePath) throws IOException {
		final StreamTemp temp = getSize(stream);
		return upload(temp.getStream(), absolutePath, temp.getSize());
	}

	public String upload(InputStream stream, String absolutePath, Long size) {
		return upload(stream, absolutePath, size, acl);
	}

	public String upload(InputStream stream, String absolutePath, Long size, ObjectCannedACL acl) {
		final String path = getPath(absolutePath);
		final PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucket).key(path);

		if (acl != null) {
			// 配置权限
			builder.acl(acl);
		}

		client.putObject(builder.build(), RequestBody.fromInputStream(stream, size));
		return path;
	}

	public void delete(String path) {
		client.deleteObject(builder -> builder.bucket(bucket).key(getPath(path)));
	}

	@SneakyThrows
	public void copy(String absoluteSource, String absoluteTarget) {
		String s = getCopyUrl(absoluteSource);
		final CopyObjectRequest request = CopyObjectRequest.builder().copySource(s).destinationBucket(bucket)
				.destinationKey(getPath(absoluteTarget)).build();
		client.copyObject(request);
	}

	public String getDownloadUrl(String absolutePath) {
		return String.format("%s/%s", downloadPrefix, getPath(absolutePath));
	}

	protected String getCopyUrl(String path) throws UnsupportedEncodingException {
		return URLEncoder.encode(bucket + getPath(path), StandardCharsets.UTF_8.toString());
	}

	@Override
	public void destroy() throws Exception {
		client.close();
	}

	/**
	 * 方便有其他更好的计算大小实现时可以先替换
	 * @param stream 要计算大小的流
	 * @return StreamTemp 返回大小和一个完全相同的新流
	 * @author lingting 2021-05-10 15:29
	 * @throws IOException 流操作时异常
	 */
	public StreamTemp getSize(InputStream stream) throws IOException {
		return StreamTemp.of(stream);
	}

	/**
	 * 获取真实文件路径
	 * @param absolutePath 文件相对 getRoot() 的路径
	 * @return 文件绝对路径
	 * @author lingting 2021-05-10 15:58
	 */
	public String getPath(String absolutePath) {
		Assert.hasText(absolutePath, "path must not be empty");

		if (absolutePath.startsWith(SLASH)) {
			absolutePath = absolutePath.substring(1);
		}

		return getRoot() + absolutePath;
	}

}
