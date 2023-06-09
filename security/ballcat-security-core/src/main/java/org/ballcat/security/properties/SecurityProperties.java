package org.ballcat.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2023-03-29 20:50
 */
@Data
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {

	public static final String PREFIX = "ballcat.security";

	/**
	 * 前后端交互使用的对称加密算法的密钥，必须 16 位字符
	 */
	private String passwordSecretKey;

	private Authorization authorization;

	@Data
	public static class Authorization {

		private boolean remote = false;

		private String remoteHost;

	}

}
