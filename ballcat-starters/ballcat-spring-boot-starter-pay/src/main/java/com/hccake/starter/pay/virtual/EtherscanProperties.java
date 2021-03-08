package com.hccake.starter.pay.virtual;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import live.lingting.virtual.currency.etherscan.endpoints.EtherscanEndpoints;

/**
 * @author lingting 2021/1/22 17:47
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay.ethereum")
public class EtherscanProperties {

	/**
	 * 是否开启
	 */
	private Boolean enabled = true;

	private EtherscanEndpoints endpoints;

	/**
	 * 使用 infura 平台. 网址 https://infura.io/dashboard
	 */
	private Infura infura;

	@Data
	public static class Infura {

		/**
		 * 在 <a href="https://infura.io/dashboard"/> 注册后, 创建的app的 projectId
		 */
		private String projectId;

		/**
		 * projectSecret
		 */
		private String projectSecret;

	}

}
