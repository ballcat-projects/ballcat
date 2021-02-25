package com.hccake.starter.pay.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/1/25 11:29
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay.wx")
public class WxPayProperties {

	/**
	 * 是否使用沙箱, 如果为 false 则使用 prod的配置初始化支付信息, 否则使用dev配置进行初始化
	 */
	private Boolean sandbox = false;

	/**
	 * 线上环境配置
	 */
	public Config prod;

	/**
	 * 沙箱配置.
	 */
	public Config dev;

	@Data
	public static class Config {

		private String appId;

		private String mchId;

		private String mckKey;

		private String returnUrl;

		private String notifyUrl;

	}

}
