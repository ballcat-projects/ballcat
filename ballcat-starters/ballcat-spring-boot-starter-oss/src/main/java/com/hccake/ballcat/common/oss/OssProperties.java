package com.hccake.ballcat.common.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/16 15:34
 */
@Data
@ConfigurationProperties(prefix = "ballcat.oss")
public class OssProperties {

	/**
	 *
	 * <p>
	 * endpoint 节点地址, 例如:
	 * </p>
	 * <ul>
	 * <li>阿里云节点: oss-cn-qingdao.aliyuncs.com</li>
	 * <li>亚马逊s3节点: s3.ap-southeast-1.amazonaws.com</li>
	 * <li>亚马逊节点: ap-southeast-1.amazonaws.com</li>
	 * </ul>
	 * <p>
	 * 只需要完整 正确的节点地址即可
	 * </p>
	 * <p>
	 * 如果使用 自定义的域名转发 不需要配置本值
	 * </p>
	 */
	private String endpoint;

	/**
	 * <p>
	 * 区域
	 * </p>
	 *
	 * <p>
	 * 如果配置了自定义域名(domain), 必须配置本值
	 * </p>
	 * <p>
	 * 如果没有配置自定义域名(domain), 配置了节点, 那么当前值可以不配置
	 * </p>
	 */
	private String region;

	/**
	 * 密钥key
	 */
	private String accessKey;

	/**
	 * 密钥Secret
	 */
	private String accessSecret;

	/**
	 * <p>
	 * bucket 必填
	 * </p>
	 */
	private String bucket;

	/**
	 * <p>
	 * 自定义域名转发
	 * </p>
	 * <p>
	 * 本值优先级最高, 配置本值后 endpoint 无效
	 * </p>
	 * <p>
	 * 配置本值必须配置 region
	 * </p>
	 */
	private String domain;

	/**
	 * 所有文件相关操作都在此路径下进行操作
	 * @deprecated use {@link OssProperties#objectKeyPrefix}
	 */
	@Deprecated
	private String rootPath = "";

	/**
	 * 所有 oss 对象 key 的前缀
	 */
	private String objectKeyPrefix = "";

	/**
	 * 上传时为文件配置acl, 为null 不配置
	 */
	private ObjectCannedACL acl;

	public String getObjectKeyPrefix() {
		String prefix = objectKeyPrefix;

		if (!StringUtils.hasText(prefix)) {
			prefix = rootPath;
		}

		// 不存在或者是 / 直接返回
		if (!StringUtils.hasText(prefix) || prefix.equals(OssConstants.SLASH)) {
			return "";
		}

		// 保证 root path 以 / 结尾
		if (!prefix.endsWith(OssConstants.SLASH)) {
			prefix = prefix + OssConstants.SLASH;
		}

		// 保证 root path 不以 / 开头
		if (prefix.startsWith(OssConstants.SLASH)) {
			prefix = prefix.substring(1);
		}

		return prefix;
	}

}
