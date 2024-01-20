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

package org.ballcat.pay.wx.constants;

import java.math.BigDecimal;

/**
 * @author lingting 2021/1/26 16:15
 */
public final class WxPayConstant {

	private WxPayConstant() {
	}

	/**
	 * 一百
	 */
	public static final BigDecimal HUNDRED = new BigDecimal("100");

	/**
	 * 签名字段名
	 */
	public static final String FIELD_SIGN = "sign";

	/**
	 * 签名类型字段名
	 */
	public static final String FIELD_SIGN_TYPE = "sign_type";

	/**
	 * 回调成功返回值
	 */
	public static final String CALLBACK_SUCCESS = "<xml>\n" + "  <return_code><![CDATA[SUCCESS]]></return_code>\n"
			+ "  <return_msg><![CDATA[OK]]></return_msg>\n" + "</xml>";

	/**
	 * 回调验签失败返回值
	 */
	public static final String CALLBACK_SIGN_ERROR = "<xml>\n" + "  <return_code><![CDATA[FAIL]]></return_code>\n"
			+ "  <return_msg><![CDATA[签名异常]]></return_msg>\n" + "</xml>";

}
