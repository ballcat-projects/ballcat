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

import java.math.BigDecimal;

import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.ballcat.pay.ali.constants.AliPayConstant;
import org.ballcat.pay.ali.enums.TradeStatus;

/**
 * 简化查询结果
 *
 * @author lingting 2021/1/26 10:34
 */
@Getter
@ToString
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class AliPayQuery {

	public static AliPayQuery of(AlipayTradeQueryResponse raw) {
		AliPayQuery query = new AliPayQuery();
		if (raw == null) {
			return query;
		}
		// 状态处理
		if (AliPayConstant.CODE_SUCCESS.equals(raw.getCode())) {
			// 成功
			query.setTradeStatus(TradeStatus.of(raw.getTradeStatus()));
		}
		// 异常
		else {
			query.setTradeStatus(TradeStatus.ERROR);
		}

		// 金额
		if (null == raw.getTotalAmount() || raw.getTotalAmount().length() == 0) {
			query.setAmount(BigDecimal.ZERO);
		}
		else {
			query.setAmount(new BigDecimal(raw.getTotalAmount()));
		}

		// 信息
		query.setCode(raw.getCode()).setMsg(raw.getMsg()).setSubCode(raw.getSubCode()).setSubMsg(raw.getSubMsg());

		// 基础数据
		return query.setTradeNo(raw.getTradeNo())
			.setSn(raw.getOutTradeNo())
			.setId(raw.getBuyerLogonId())
			.setUserId(raw.getBuyerUserId())
			.setUserName(raw.getBuyerUserName())
			.setUserType(raw.getBuyerUserType());
	}

	/**
	 * 原始数据
	 */
	private AlipayTradeQueryResponse raw;

	/**
	 * 订单状态
	 */
	private TradeStatus tradeStatus;

	private String code;

	private String msg;

	private String subCode;

	private String subMsg;

	/**
	 * 金额(单位: 元)
	 */
	private BigDecimal amount;

	/**
	 * 平台订单号
	 */
	private String sn;

	/**
	 * 支付宝订单号
	 */
	private String tradeNo;

	/**
	 * 支付用户支付宝账号信息
	 */
	private String id;

	/**
	 * 支付用户id
	 */
	private String userId;

	private String userName;

	private String userType;

}
