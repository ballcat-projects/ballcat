package com.hccake.starte.pay.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求后缀
 * @author lingting 2021/1/29 18:01
 */
@Getter
@AllArgsConstructor
public enum RequestSuffix {

	/**
	 * 下单请求后缀
	 */
	UNIFIEDORDER("unifiedorder"),
	/**
	 * 获取沙箱密钥
	 */
	GETSIGNKEY("getsignkey"),
	/**
	 * 查询订单
	 */
	ORDERQUERY("orderquery"),

	;

	/**
	 * 后缀
	 */
	private final String suffix;

}
