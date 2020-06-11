package com.hccake.starter.ding.talk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting  2020/6/11 23:19
 */
@Data
@ConfigurationProperties(prefix = "ballcat.ding-talk")
public class DingTalkProperties {
	/**
	 * Web hook 地址
	 */
	private String url;
	/**
	 * 密钥
	 */
	private String secret;
}
