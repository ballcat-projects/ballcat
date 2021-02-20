package com.hccake.starter.pay.virtual;

import live.lingting.virtual.currency.endpoints.TronscanEndpoints;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/1/22 17:47
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay.tronscan")
public class TronscanProperties {

	/**
	 * 是否开启
	 */
	private Boolean enabled = true;

	/**
	 * 节点
	 */
	private TronscanEndpoints endpoints;

}
