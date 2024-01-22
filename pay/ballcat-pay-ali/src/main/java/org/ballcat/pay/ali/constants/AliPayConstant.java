/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.pay.ali.constants;

/**
 * @author lingting 2021/1/25 13:35
 */
public final class AliPayConstant {

	private AliPayConstant() {
	}

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
