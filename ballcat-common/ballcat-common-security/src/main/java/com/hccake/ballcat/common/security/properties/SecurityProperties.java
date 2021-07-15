package com.hccake.ballcat.common.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/18 10:55 安全相关配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {

	public static final String PREFIX = "ballcat.security";

	/**
	 * 前后端交互使用的对称加密算法的密钥，必须 16 位字符
	 */
	private String passwordSecretKey;

}
