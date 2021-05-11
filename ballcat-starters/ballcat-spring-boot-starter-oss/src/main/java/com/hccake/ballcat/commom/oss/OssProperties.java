package com.hccake.ballcat.commom.oss;

import static com.hccake.ballcat.commom.oss.OssConstants.PATH_FLAG;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

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
	private String bucketName;

	/**
	 * 所有文件相关操作都在此路径下进行操作
	 */
	private String rootPath = PATH_FLAG;

	public String getRootPath() {
		if (!StringUtils.hasText(rootPath)) {
			rootPath = PATH_FLAG;
		}

		// 保证 root path 以 / 结尾
		if (!rootPath.endsWith(PATH_FLAG)) {
			rootPath = rootPath + PATH_FLAG;
		}

		// 保证 root path 以 / 开头
		if (!rootPath.startsWith(PATH_FLAG)) {
			rootPath = PATH_FLAG + rootPath;
		}

		return rootPath;
	}

}
