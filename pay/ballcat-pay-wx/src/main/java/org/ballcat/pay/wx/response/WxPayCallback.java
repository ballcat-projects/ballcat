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

package org.ballcat.pay.wx.response;

import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.pay.wx.WxPay;
import org.ballcat.pay.wx.enums.ResponseCode;
import org.ballcat.pay.wx.enums.TradeType;

/**
 * @author lingting 2021/2/25 15:43
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WxPayCallback {

	private String transactionId;

	private String nonceStr;

	private String bankType;

	private String openid;

	private String sign;

	private String feeType;

	private String mchId;

	private BigInteger cashFee;

	private String outTradeNo;

	private String appid;

	private BigInteger totalFee;

	private TradeType tradeType;

	private ResponseCode resultCode;

	private String timeEnd;

	private String isSubscribe;

	private ResponseCode returnCode;

	public static WxPayCallback of(Map<String, String> res) {
		return JsonUtils.toObj(JsonUtils.toJson(res), WxPayCallback.class).setRaw(res);
	}

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

	/**
	 * 验签
	 * @param wxPay 微信支付信息
	 * @return boolean
	 */
	public boolean checkSign(WxPay wxPay) {
		return wxPay.checkSign(this);
	}

}
