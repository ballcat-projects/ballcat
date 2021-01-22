package com.hccake.starter.pay.virtual;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import live.lingting.virtual.currency.endpoints.BitcoinEndpoints;
import live.lingting.virtual.currency.endpoints.OmniEndpoints;

/**
 * @author lingting 2021/1/22 17:45
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay.bitcoin")
public class BitcoinProperties {

	/**
	 * 是否开启
	 */
	private Boolean enabled = true;

	/**
	 * 使用 omni 平台
	 */
	private Omni omni;

	/**
	 * 比特节点
	 */
	private BitcoinEndpoints endpoints;

	@Data
	public static class Omni {

		/**
		 * 节点
		 */
		private OmniEndpoints endpoints;

	}

}
