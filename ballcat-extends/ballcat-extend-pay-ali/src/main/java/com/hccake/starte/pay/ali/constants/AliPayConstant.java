package com.hccake.starte.pay.ali.constants;

import java.math.BigDecimal;

/**
 * @author lingting 2021/1/25 13:35
 */
public class AliPayConstant {

	/**
	 * 一百
	 */
	public static final BigDecimal HUNDRED = new BigDecimal("100");

	/**
	 * 支付宝支付线上api路径
	 */
	public static final String SERVER_URL_PROD = "https://openapi.alipay.com/gateway.do";

	/**
	 * 支付宝支付沙箱api路径
	 */
	public static final String SERVER_URL_DEV = "https://openapi.alipaydev.com/gateway.do";

	/**
	 * 查询支付成功返回code
	 */
	public static final String CODE_SUCCESS = "10000";

}
