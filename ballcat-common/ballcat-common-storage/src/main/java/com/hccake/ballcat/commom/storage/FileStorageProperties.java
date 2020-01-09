package com.hccake.ballcat.commom.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/7/16 15:34
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

	/**
	 * endpoint 服务地址 http://oss-cn-qingdao.aliyuncs.com
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

}
