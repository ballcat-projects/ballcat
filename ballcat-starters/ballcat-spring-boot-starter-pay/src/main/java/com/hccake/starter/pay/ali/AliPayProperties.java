package com.hccake.starter.pay.ali;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/1/25 11:29
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay.ali")
public class AliPayProperties {

	/**
	 * 是否使用沙箱, 如果为 false 则使用 prod的配置初始化支付信息, 否则使用dev配置进行初始化
	 */
	private Boolean sandbox = false;

	/**
	 * 线上环境配置
	 */
	private Config prod;

	/**
	 * 沙箱配置
	 */
	private Config dev;

	@Data
	public static class Config {

		private String appId;

		/**
		 * rsa私钥(应用私钥)
		 */
		private String privateKey;

		private String format = "json";

		private String charset = "utf-8";

		/**
		 * 支付宝公钥
		 */
		private String alipayPublicKey;

		private String signType = "RSA2";

		private String returnUrl;

		private String notifyUrl;

	}

}
