package com.hccake.ballcat.commom.storage.client;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hccake.ballcat.commom.storage.FileStorageClient;
import java.io.InputStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/16 15:45
 */
@Getter
@RequiredArgsConstructor
public class AliyunOssClient implements FileStorageClient, InitializingBean, DisposableBean {

	private final String endpoint;

	private final String accessKey;

	private final String accessSecret;

	private final String bucketName;

	private final String root;

	private OSS client;

	@Override
	public String upload(InputStream stream, String filePath, Long size) {
		final String path = getPath(filePath);
		client.putObject(bucketName, path, stream);
		return path;
	}

	@Override
	public void delete(String path) {
		path = getPath(path);
		if (client.doesObjectExist(bucketName, path)) {
			client.deleteObject(bucketName, path);
		}
	}

	@Override
	public void copy(String source, String target) {
		client.copyObject(bucketName, getPath(source), bucketName, getPath(target));
	}

	@Override
	public String getDownloadUrl(String filePath) {
		return String.format("https://%s.%s/%s", bucketName, endpoint, getPath(filePath));
	}

	@Override
	public void afterPropertiesSet() {
		Assert.hasText(endpoint, "endpoint 为空");
		Assert.hasText(accessKey, "Oss accessKey为空");
		Assert.hasText(accessSecret, "Oss accessSecret为空");
		client = new OSSClientBuilder().build(endpoint, accessKey, accessSecret);
	}

	@Override
	public void destroy() {
		if (this.client != null) {
			this.client.shutdown();
		}
	}

}
