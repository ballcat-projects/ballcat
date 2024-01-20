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

package org.ballcat.pay.ali.domain;

import com.alipay.api.response.AlipayTradeAppPayResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * app 拉起支付宝支付需要的信息
 *
 * @author lingting 2021/1/25 10:52
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliAppPay {

	private String id;

	private String require;

	/**
	 * 构建 app 拉起支付宝支付所需的参数
	 * @param appId app id
	 * @param response app pay 接口返回值
	 * @return {@link AliAppPay}
	 */
	public static AliAppPay of(String appId, AlipayTradeAppPayResponse response) {
		return of(appId, response.getBody());
	}

	/**
	 * 构建 app 拉起支付宝支付所需的参数
	 * @param appId app id
	 * @param body app pay 接口返回值中 body 字段内容
	 * @return {@link AliAppPay}
	 */
	public static AliAppPay of(String appId, String body) {
		AliAppPay pay = new AliAppPay();
		pay.setId(appId);
		pay.setRequire(body);
		return pay;
	}

}
