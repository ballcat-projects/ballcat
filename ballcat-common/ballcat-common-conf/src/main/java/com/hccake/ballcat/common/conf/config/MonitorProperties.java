package com.hccake.ballcat.common.conf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "monitor")
public class MonitorProperties {

	private Actuator actuator;

	@Data
	public static class Actuator {

		private Auth auth;

		@Data
		public static class Auth {

			/**
			 * 是否开启.
			 */
			private Boolean enabled = true;

			private String secretId;

			private String secretKey;

		}

	}

}
