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

package org.ballcat.pay.ali.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易状态
 */
@Getter
@AllArgsConstructor
public enum TradeStatus {

	/**
	 * 成功
	 */
	SUCCESS("TRADE_SUCCESS"),
	/**
	 * 未支付
	 */
	WAIT("WAIT_BUYER_PAY"),
	/**
	 * 未付款交易超时关闭，或支付完成后全额退款
	 */
	CLOSED("TRADE_CLOSED"),
	/**
	 * 交易结束，不可退款
	 */
	FINISHED("TRADE_FINISHED"),
	/**
	 * 异常. 具体信息查询 subCode和subMsg
	 */
	ERROR("");

	private final String str;

	@JsonCreator
	public static TradeStatus of(String status) {
		if (null == status) {
			return ERROR;
		}

		for (TradeStatus e : values()) {
			if (e.getStr().equals(status)) {
				return e;
			}
		}

		return ERROR;
	}

}
