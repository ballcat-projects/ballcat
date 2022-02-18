package com.hccake.ballcat.common.oss;

import com.hccake.ballcat.common.oss.domain.StreamTemp;
import com.hccake.ballcat.common.oss.exception.OssDisabledException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Getter
public class OssClient implements DisposableBean {

	private final String endpoint;

	private final String region;

	private final String accessKey;

	private final String accessSecret;

	private final String bucket;

	private final String domain;

	private final String objectKeyPrefix;

	private final S3Client client;

	private final ObjectCannedACL acl;

	private final String downloadPrefix;

	private final boolean enable;

	public OssClient(OssProperties properties) {
		this.endpoint = properties.getEndpoint();
		this.region = properties.getRegion();
		this.accessKey = properties.getAccessKey();
		this.accessSecret = properties.getAccessSecret();
		this.bucket = properties.getBucket();
		this.domain = properties.getDomain();
		this.objectKeyPrefix = properties.getObjectKeyPrefix();
		this.acl = properties.getAcl();

		final boolean disabled = !StringUtils.hasText(accessKey) || !StringUtils.hasText(accessSecret)
				|| (!StringUtils.hasText(endpoint) && !StringUtils.hasText(domain));
		S3Client tempClient = null;
		String tempDownloadPrefix = "";
		if (!disabled) {
			final ClientBuilder builder = createBuilder();
			try {
				tempClient = builder.build();
				tempDownloadPrefix = builder.downloadPrefix();
			}
			catch (Exception e) {
				log.error("oss构造失败!", e);
				tempClient = null;
			}
		}

		this.client = tempClient;
		this.enable = !disabled;
		this.downloadPrefix = tempDownloadPrefix;
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
	 * 文件上传, 本方法会读一遍流, 计算流大小, 推荐使用 upload(stream, relativeKey, size) 方法
	 * <h1>注意: 本方法不会主动关闭流. 请手动关闭传入的流</h1>
	 * @param relativeKey 文件相对 getRoot() 的路径
	 * @param stream 文件输入流
	 * @return 文件绝对路径
	 * @throws IOException 流操作时异常
	 */
	public String upload(InputStream stream, String relativeKey) throws IOException {
		final StreamTemp temp = getSize(stream);
		try (final InputStream tempStream = temp.getStream()) {
			return upload(tempStream, relativeKey, temp.getSize());
		}
	}

	/**
	 * 通过流上传文件
	 * <h1>注意: 本方法不会主动关闭流. 请手动关闭传入的流</h1>
	 * @param stream 流
	 * @param relativeKey 相对key
	 * @param size 流大小
	 * @return java.lang.String
	 */
	public String upload(InputStream stream, String relativeKey, Long size) {
		return upload(stream, relativeKey, size, acl);
	}

	/**
	 * 通过文件对象上传文件
	 * @param file 文件
	 * @param relativeKey 相对key
	 * @return java.lang.String
	 * @throws IOException 流操作时异常
	 */
	public String upload(File file, String relativeKey) throws IOException {
		try (final FileInputStream stream = new FileInputStream(file)) {
			return upload(stream, relativeKey, Files.size(file.toPath()), acl);
		}
	}

	/**
	 * 通过流上传文件
	 * <h1>注意: 本方法不会主动关闭流. 请手动关闭传入的流</h1>
	 * @param stream 流
	 * @param relativeKey 相对key
	 * @param size 流大小
	 * @param acl 文件权限
	 * @return java.lang.String
	 */
	public String upload(InputStream stream, String relativeKey, Long size, ObjectCannedACL acl) {
		final String objectKey = getObjectKey(relativeKey);
		final PutObjectRequest.Builder builder = PutObjectRequest.builder().bucket(bucket).key(objectKey);

		if (acl != null) {
			// 配置权限
			builder.acl(acl);
		}

		getClient().putObject(builder.build(), RequestBody.fromInputStream(stream, size));
		return objectKey;
	}

	public void delete(String objectKey) {
		getClient().deleteObject(builder -> builder.bucket(bucket).key(getObjectKey(objectKey)));
	}

	@SneakyThrows
	public void copy(String absoluteSource, String absoluteTarget) {
		String s = getCopyUrl(absoluteSource);
		final CopyObjectRequest request = CopyObjectRequest.builder().copySource(s).destinationBucket(bucket)
				.destinationKey(getObjectKey(absoluteTarget)).build();
		getClient().copyObject(request);
	}

	/**
	 * 获取 相对路径 的下载url
	 * @author lingting 2021-05-12 18:50
	 */
	public String getDownloadUrl(String relativeKey) {
		return getDownloadUrlByAbsolute(getObjectKey(relativeKey));
	}

	/**
	 * 获取 绝对路径 的下载url
	 * @author lingting 2021-05-12 18:50
	 */
	public String getDownloadUrlByAbsolute(String objectKey) {
		return String.format("%s/%s", downloadPrefix, objectKey);
	}

	protected String getCopyUrl(String objectKey) throws UnsupportedEncodingException {
		return URLEncoder.encode(bucket + getObjectKey(objectKey), StandardCharsets.UTF_8.toString());
	}

	/**
	 * 检查oss是否启用
	 * @author lingting 2021-05-27 10:45
	 */
	public boolean enabled() {
		return enable;
	}

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
	 * @deprecated use {@link OssClient#getObjectKey}
	 */
	@Deprecated
	public String getPath(String relativePath) {
		return getObjectKey(relativePath);
	}

	/**
	 * 获取真实Oss对象key
	 * @param relativeKey 文件相对 getRoot() 的Key
	 * @return 文件绝对路径
	 * @author lingting 2021-05-10 15:58
	 */
	public String getObjectKey(String relativeKey) {
		Assert.hasText(relativeKey, "key must not be empty");

		if (relativeKey.startsWith(OssConstants.SLASH)) {
			relativeKey = relativeKey.substring(1);
		}

		return getObjectKeyPrefix() + relativeKey;
	}

}
