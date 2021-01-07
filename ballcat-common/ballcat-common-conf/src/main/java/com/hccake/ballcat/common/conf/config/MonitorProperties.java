package com.hccake.ballcat.common.conf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@ConfigurationProperties(prefix = "ballcat")
public class MonitorProperties {

	private Actuator actuator;

	@Data
	public static class Actuator {

		/**
		 * 是否开启.
		 */
		private Boolean auth = false;

		private String secretId;

		private String secretKey;

	}

}
