package com.hccake.starter.pay.virtual;

import live.lingting.virtual.currency.bitcoin.endpoints.BitcoinEndpoints;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
	 * 比特节点
	 */
	private BitcoinEndpoints endpoints;

}
