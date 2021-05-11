package com.hccake.ballcat.commom.oss;

import static com.hccake.ballcat.commom.oss.OssConstants.PATH_FLAG;

import com.hccake.ballcat.commom.oss.domain.StreamTemp;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

/**
 * @author lingting 2021/5/11 9:59
 */
@Getter
public class OssClient implements DisposableBean {

	private final String endpoint;

	private final String accessKey;

	private final String accessSecret;

	private final String bucketName;

	private final String root;

	private final S3Client client;

	public OssClient(String endpoint, String accessKey, String accessSecret, String bucketName, String root) {
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
		final String path = getPath(absolutePath);
		client.putObject(builder -> builder.bucket(bucketName).key(path), RequestBody.fromInputStream(stream, size));
		return path;
	}

	public void delete(String path) {
		client.deleteObject(builder -> builder.bucket(bucketName).key(getPath(path)));
	}

	@SneakyThrows
	public void copy(String absoluteSource, String absoluteTarget) {
		String s = getCopyUrl(absoluteSource);
		client.copyObject(
				builder -> builder.copySource(s).destinationBucket(bucketName).destinationKey(getPath(absoluteTarget)));
	}

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

		if (absolutePath.startsWith(PATH_FLAG)) {
			absolutePath = absolutePath.substring(1);
		}

		return getRoot() + absolutePath;
	}

}
