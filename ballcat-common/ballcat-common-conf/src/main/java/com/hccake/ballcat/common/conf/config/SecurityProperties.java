package com.hccake.ballcat.common.conf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@ConfigurationProperties(prefix = "ballcat.security")
public class SecurityProperties {

	private Xss xss;

	@Data
	public static class Xss {

		/**
		 * 是否开启
		 */
		private Boolean enabled = true;

	}

}
