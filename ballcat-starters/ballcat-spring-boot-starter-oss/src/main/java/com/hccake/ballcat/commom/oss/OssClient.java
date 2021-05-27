package com.hccake.ballcat.commom.oss;

import static com.hccake.ballcat.commom.oss.OssConstants.SLASH;

import com.hccake.ballcat.commom.oss.domain.StreamTemp;
import com.hccake.ballcat.commom.oss.exception.OssDisabledException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * @author lingting 2021/5/11 9:59
 */
@Getter
public class OssClient implements DisposableBean {

	private final String endpoint;

	private final String region;

	private final String accessKey;

	private final String accessSecret;

	private final String bucket;

	private final String domain;

	private final String root;

	private final S3Client client;

	private final ObjectCannedACL acl;

	private String downloadPrefix;

	private boolean enable = true;

	public OssClient(OssProperties properties) {
		this.endpoint = properties.getEndpoint();
		this.region = properties.getRegion();
		this.accessKey = properties.getAccessKey();
		this.accessSecret = properties.getAccessSecret();
		this.bucket = properties.getBucket();
		this.domain = properties.getDomain();
		this.root = properties.getRootPath();
		this.acl = properties.getAcl();

		final boolean isEnable = !StringUtils.hasText(accessKey) || !StringUtils.hasText(accessSecret)
				|| (!StringUtils.hasText(endpoint) && !StringUtils.hasText(domain));
		if (isEnable) {
			this.enable = false;
			client = null;
		}
		else {
			final ClientBuilder builder = createBuilder();
			client = builder.build();
			downloadPrefix = builder.downloadPrefix();
			// 不以 / 结尾
			if (downloadPrefix.endsWith(SLASH)) {
				downloadPrefix = downloadPrefix.substring(0, downloadPrefix.length() - 1);
			}
		}
	}

	/**
	 * 生成 builder . 便于子类重写
	 * @author lingting 2021-05-13 14:43
	 */
	protected ClientBuilder createBuilder() {
		return ClientBuilder.builder().accessKey(accessKey).accessSecret(accessSecret).bucket(bucket).domain(domain)
				.endpoint(endpoint).region(region);
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

		getClient().putObject(builder.build(), RequestBody.fromInputStream(stream, size));
		return path;
	}

	public void delete(String path) {
		getClient().deleteObject(builder -> builder.bucket(bucket).key(getPath(path)));
	}

	@SneakyThrows
	public void copy(String absoluteSource, String absoluteTarget) {
		String s = getCopyUrl(absoluteSource);
		final CopyObjectRequest request = CopyObjectRequest.builder().copySource(s).destinationBucket(bucket)
				.destinationKey(getPath(absoluteTarget)).build();
		getClient().copyObject(request);
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

	/**
	 * 检查oss是否启用
	 * @author lingting 2021-05-27 10:45
	 */
	public boolean enabled() {
		return enable;
	}

	@SneakyThrows
	protected S3Client getClient() {
		if (client == null) {
			throw new OssDisabledException();
		}
		return client;
	}

	@Override
	public void destroy() throws Exception {
		if (client != null) {
			client.close();
		}
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
