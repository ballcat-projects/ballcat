package com.hccake.ballcat.autoconfigure.web.actuate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@ConfigurationProperties(prefix = ActuatorSecurityProperties.PREFIX)
public class ActuatorSecurityProperties {

	public static final String PREFIX = "ballcat.actuator";

	/**
	 * 是否开启鉴权
	 */
	private boolean auth = false;

	private String secretId;

	private String secretKey;

}
