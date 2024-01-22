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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.pay.ali.AliPay;
import org.ballcat.pay.ali.constants.AliPayConstant;
import org.ballcat.pay.ali.enums.TradeStatus;

/**
 * @author lingting 2021/1/26 13:31
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AliPayCallback {

	/**
	 * 解析回调参数
	 * @param callbackParams 所有回调参数
	 * @return 阿里支付回调信息
	 */
	public static AliPayCallback of(Map<String, String> callbackParams) {
		Map<String, Object> map = new HashMap<>(callbackParams);
		String fundBillListStr = callbackParams.get(AliPayConstant.FIELD_FUND_BILL_LIST).replace("&quot;", "\"");
		map.put(AliPayConstant.FIELD_FUND_BILL_LIST, JsonUtils.toObj(fundBillListStr, List.class));
		// 覆盖原值
		callbackParams.put(AliPayConstant.FIELD_FUND_BILL_LIST, fundBillListStr);
		return JsonUtils.toObj(JsonUtils.toJson(map), AliPayCallback.class).setRaw(callbackParams);
	}

	public boolean checkSign(AliPay aliPay) throws AlipayApiException {
		return aliPay.checkSignV1(getRaw()) || aliPay.checkSignV2(getRaw());
	}

	@JsonIgnore
	private Map<String, String> raw;

	private String gmtCreate;

	private String charset;

	private String sellerEmail;

	private String subject;

	private String sign;

	private String buyerId;

	private BigDecimal invoiceAmount;

	private String notifyId;

	private List<FundBill> fundBillList;

	private String notifyType;

	private TradeStatus tradeStatus;

	private BigDecimal receiptAmount;

	private String appId;

	private BigDecimal buyerPayAmount;

	private String signType;

	private String sellerId;

	private String gmtPayment;

	private String notifyTime;

	private String version;

	private String outTradeNo;

	private BigDecimal totalAmount;

	private String tradeNo;

	private String authAppId;

	private String buyerLogonId;

	private BigDecimal pointAmount;

	@Data
	public static class FundBill {

		private BigDecimal amount;

		private String fundChannel;

	}

}
