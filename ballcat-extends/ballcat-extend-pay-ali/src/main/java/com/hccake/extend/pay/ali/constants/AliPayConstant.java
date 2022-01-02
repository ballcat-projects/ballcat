package com.hccake.extend.pay.ali.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2021/1/25 13:35
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliPayConstant {

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

	/**
	 * 用于电脑网页支付时的产品码
	 */
	public static final String PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

	public static final String FIELD_FUND_BILL_LIST = "fund_bill_list";

}
