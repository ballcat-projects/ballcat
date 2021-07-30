package com.hccake.ballcat.autoconfigure.web.actuate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2020-10-13 22:39
 */
@Data
@ConfigurationProperties(prefix = "ballcat.actuator")
public class ActuatorSecurityProperties {

	/**
	 * 是否开启鉴权
	 */
	private Boolean auth = false;

	private String secretId;

	private String secretKey;

}
