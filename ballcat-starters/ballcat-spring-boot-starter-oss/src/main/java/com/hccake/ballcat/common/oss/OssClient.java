package com.hccake.ballcat.common.oss;

import com.hccake.ballcat.common.oss.prefix.ObjectKeyPrefixConverter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author lingting 2021/5/11 9:59
 */
@Deprecated
@RequiredArgsConstructor
public class OssClient {

	private final OssTemplate ossTemplate;

	private final ObjectKeyPrefixConverter objectKeyPrefixConverter;

	public boolean isEnable() {
		return ossTemplate.getOssProperties().getEnabled();
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
		return upload(stream, relativeKey, size, null);
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
			return upload(stream, relativeKey, Files.size(file.toPath()), null);
		}
	}

	/**
	 * 通过流上传文件
	 * <h1>注意: 本方法不会主动关闭流. 请手动关闭传入的流</h1>
	 * @param stream 流
	 * @param relativeKey 相对key,会自动尝试包装全局前缀
	 * @param size 流大小
	 * @param acl 文件权限
	 * @return java.lang.String
	 */
	public String upload(InputStream stream, String relativeKey, Long size, ObjectCannedACL acl) {
		final String objectKey = objectKeyPrefixConverter.wrap(relativeKey);
		final PutObjectRequest.Builder builder = PutObjectRequest.builder()
			.bucket(ossTemplate.getOssProperties().getBucket())
			.key(objectKey);

		if (acl != null) {
			// 配置权限
			builder.acl(acl);
		}

		ossTemplate.putObject(builder.build(), RequestBody.fromInputStream(stream, size));
		return objectKey;
	}

	/**
	 * 获取 相对路径 的下载url
	 */
	public String getDownloadUrl(String relativeKey) {
		return getDownloadUrlByAbsolute(objectKeyPrefixConverter.wrap(relativeKey));
	}

	/**
	 * 获取 绝对路径 的下载url
	 */
	public String getDownloadUrlByAbsolute(String objectKey) {
		return String.format("%s/%s", ossTemplate.getOssProperties().getEndpoint(), objectKey);
	}

}
