package com.hccake.ballcat.common.oss;

import com.hccake.ballcat.common.oss.interceptor.ModifyPathInterceptor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

/**
 * @author lingting 2021/5/12 22:01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBuilder {

	private String endpoint;

	private String regionStr;

	private String accessKey;

	private String accessSecret;

	private String bucket;

	private String domain;

	private String downloadPrefix;

	public static ClientBuilder builder() {
		return new ClientBuilder();
	}

	public String endpoint() {
		return endpoint;
	}

	public ClientBuilder endpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public String region() {
		return regionStr;
	}

	public ClientBuilder region(String region) {
		this.regionStr = region;
		return this;
	}

	public String accessKey() {
		return accessKey;
	}

	public ClientBuilder accessKey(String accessKey) {
		this.accessKey = accessKey;
		return this;
	}

	public String accessSecret() {
		return accessSecret;
	}

	public ClientBuilder accessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
		return this;
	}

	public String bucket() {
		return bucket;
	}

	public ClientBuilder bucket(String bucket) {
		this.bucket = bucket;
		return this;
	}

	public String downloadPrefix() {
		// 不以 / 结尾
		if (StringUtils.hasText(downloadPrefix) && downloadPrefix.endsWith(OssConstants.SLASH)) {
			return downloadPrefix.substring(0, downloadPrefix.length() - 1);
		}
		return downloadPrefix;
	}

	public ClientBuilder downloadPrefix(String downloadPrefix) {
		this.downloadPrefix = downloadPrefix;
		return this;
	}

	public String domain() {
		return domain;
	}

	public ClientBuilder domain(String domain) {
		this.domain = domain;
		return this;
	}

	private S3ClientBuilder create() throws URISyntaxException {
		S3ClientBuilder builder = S3Client.builder();

		// 关闭路径形式
		builder.serviceConfiguration(sb -> sb.pathStyleAccessEnabled(false).chunkedEncodingEnabled(false));

		String uriStr = domain;

		// 未使用自定义域名
		if (!StringUtils.hasText(uriStr)) {
			uriStr = endpoint;

			// 亚马逊节点
			if (endpoint.contains(OssConstants.AWS_INTERNATIONAL)
					// 不是s3节点
					&& !endpoint.startsWith(OssConstants.S3)) {
				uriStr = OssConstants.S3 + endpoint;
			}

			final Region region;
			if (StringUtils.hasText(regionStr)) {
				// 配置了区域
				region = Region.of(regionStr);
			}
			else {
				// 未配置, 通过节点解析
				regionStr = uriStr.startsWith(OssConstants.S3)
						// 亚马逊s3节点
						? uriStr.substring(OssConstants.S3.length(),
								uriStr.indexOf(OssConstants.DOT, OssConstants.S3.length() + 1))
						// 其他节点
						: uriStr.substring(0, uriStr.indexOf(OssConstants.DOT));

				region = Region.of(regionStr);
			}

			builder.region(region);

			// 使用托管形式 参考文档
			// https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/VirtualHosting.html
			uriStr = "https://" + bucket + "." + uriStr;
		}
		else {
			// 使用自定义域名
			Assert.hasText(regionStr, "使用自定义域名时, 区域不能为空!");
			builder.region(Region.of(regionStr));
		}

		// 配置下载地址前缀
		downloadPrefix(uriStr);

		builder.endpointOverride(new URI(uriStr)).credentialsProvider(
				StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, accessSecret)));

		// 配置
		builder.overrideConfiguration(cb -> cb.addExecutionInterceptor(new ModifyPathInterceptor(bucket)));

		return builder;
	}

	public S3Client build() throws URISyntaxException {
		return create().build();
	}

	/**
	 * 覆写一些配置
	 * @author lingting 2021-05-12 22:37
	 */
	public S3Client build(Consumer<S3ClientBuilder> consumer) throws URISyntaxException {
		final S3ClientBuilder builder = create();
		consumer.accept(builder);
		return builder.build();
	}

}
