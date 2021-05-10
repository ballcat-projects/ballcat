package com.hccake.ballcat.commom.storage.client;

import com.hccake.ballcat.commom.storage.FileStorageClient;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.DisposableBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author lingting 2021/5/10 14:05
 */
@Getter
public class AwsOssClient implements FileStorageClient, DisposableBean {

	private final String endpoint;

	private final String accessKey;

	private final String accessSecret;

	private final String bucketName;

	private final String root;

	private final S3Client client;

	public AwsOssClient(String endpoint, String accessKey, String accessSecret, String bucketName, String root) {
		this.endpoint = endpoint;
		this.accessKey = accessKey;
		this.accessSecret = accessSecret;
		this.bucketName = bucketName;
		this.root = root;
		client = S3Client.builder()
				// 节点
				.region(Region.of(endpoint))
				// key secret
				.credentialsProvider(
						StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, accessSecret)))
				.build();
	}

	@Override
	public String upload(InputStream stream, String absolutePath, Long size) {
		final String path = getPath(absolutePath);
		client.putObject(builder -> builder.bucket(bucketName).key(path), RequestBody.fromInputStream(stream, size));
		return path;
	}

	@Override
	public void delete(String path) {
		client.deleteObject(builder -> builder.bucket(bucketName).key(getPath(path)));
	}

	@SneakyThrows
	@Override
	public void copy(String absoluteSource, String absoluteTarget) {
		String s = getCopyUrl(absoluteSource);
		client.copyObject(
				builder -> builder.copySource(s).destinationBucket(bucketName).destinationKey(getPath(absoluteTarget)));
	}

	@Override
	public String getDownloadUrl(String absolutePath) {
		return String.format("https://%s.%s/%s", bucketName, endpoint, getPath(absolutePath));
	}

	protected String getCopyUrl(String path) throws UnsupportedEncodingException {
		return URLEncoder.encode(bucketName + getPath(path), StandardCharsets.UTF_8.toString());
	}

	@Override
	public void destroy() throws Exception {
		client.close();
	}

}
