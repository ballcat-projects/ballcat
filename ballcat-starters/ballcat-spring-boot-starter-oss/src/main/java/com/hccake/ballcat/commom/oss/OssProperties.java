package com.hccake.ballcat.commom.oss;

import static com.hccake.ballcat.commom.oss.OssConstants.PATH_FLAG;

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
@ConfigurationProperties(prefix = "ballcat.file.storage")
public class OssProperties {

	/**
	 * endpoint 服务地址 eg. http://oss-cn-qingdao.aliyuncs.com
	 */
	private String endpoint;

	/**
	 * 密钥key
	 */
	private String accessKey;

	/**
	 * 密钥Secret
	 */
	private String accessSecret;

	/**
	 * bucketName
	 */
	private String bucket;

	/**
	 * 所有文件相关操作都在此路径下进行操作
	 */
	private String rootPath = PATH_FLAG;

	/**
	 * 上传时为文件配置acl, 为null 不配置
	 */
	private ObjectCannedACL acl;

	public String getRootPath() {
		if (!StringUtils.hasText(rootPath)) {
			rootPath = PATH_FLAG;
		}

		// 保证 root path 以 / 结尾
		if (!rootPath.endsWith(PATH_FLAG)) {
			rootPath = rootPath + PATH_FLAG;
		}

		// 保证 root path 不以 / 开头
		if (rootPath.startsWith(PATH_FLAG)) {
			rootPath = rootPath.substring(1);
		}

		return rootPath;
	}

}
