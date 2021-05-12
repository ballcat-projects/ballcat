package com.hccake.ballcat.commom.oss;

import static com.hccake.ballcat.commom.oss.OssConstants.AWS_INTERNATIONAL;
import static com.hccake.ballcat.commom.oss.OssConstants.DOT;
import static com.hccake.ballcat.commom.oss.OssConstants.SLASH;

import com.hccake.ballcat.commom.oss.domain.StreamTemp;
import com.hccake.ballcat.commom.oss.interceptor.BallcatExecutionInterceptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
		builder.overrideConfiguration(cb -> cb.addExecutionInterceptor(
				new BallcatExecutionInterceptor(endpoint, accessKey, accessSecret, bucket, root)));

		client = builder
				// key secret
				.credentialsProvider(
						StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, accessSecret)))
				.build();
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
	 * 文件上传, 本方法会读一遍流, 计算流大小, 推荐使用 upload(stream, relativePath, size) 方法
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @throws IOException 流操作时异常
	 */
	public String upload(InputStream stream, String relativePath) throws IOException {
		final StreamTemp temp = getSize(stream);
		return upload(temp.getStream(), relativePath, temp.getSize());
	}

	public String upload(InputStream stream, String relativePath, Long size) {
		return upload(stream, relativePath, size, acl);
	}

	public String upload(InputStream stream, String relativePath, Long size, ObjectCannedACL acl) {
		final String path = getPath(relativePath);
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

	/**
	 * 获取 相对路径 的下载url
	 * @author lingting 2021-05-12 18:50
	 */
	public String getDownloadUrl(String relativePath) {
		return getDownloadUrlByAbsolute(getPath(relativePath));
	}

	/**
	 * 获取 绝对路径 的下载url
	 * @author lingting 2021-05-12 18:50
	 */
	public String getDownloadUrlByAbsolute(String path) {
		return String.format("%s/%s", downloadPrefix, path);
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
	 * @param relativePath 文件相对 getRoot() 的路径
	 * @return 文件绝对路径
	 * @author lingting 2021-05-10 15:58
	 */
	public String getPath(String relativePath) {
		Assert.hasText(relativePath, "path must not be empty");

		if (relativePath.startsWith(SLASH)) {
			relativePath = relativePath.substring(1);
		}

		return getRoot() + relativePath;
	}

}
