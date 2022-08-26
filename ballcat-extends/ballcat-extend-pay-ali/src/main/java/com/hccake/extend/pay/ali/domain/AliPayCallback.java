package com.hccake.extend.pay.ali.domain;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.extend.pay.ali.AliPay;
import com.hccake.extend.pay.ali.enums.TradeStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hccake.extend.pay.ali.constants.AliPayConstant.FIELD_FUND_BILL_LIST;

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
	 * @return com.hccake.extend.pay.ali.domain.AliPayCallback
	 */
	public static AliPayCallback of(Map<String, String> callbackParams) {
		Map<String, Object> map = new HashMap<>(callbackParams);
		String fundBillListStr = callbackParams.get(FIELD_FUND_BILL_LIST).replace("&quot;", "\"");
		map.put(FIELD_FUND_BILL_LIST, JsonUtils.toObj(fundBillListStr, List.class));
		// 覆盖原值
		callbackParams.put(FIELD_FUND_BILL_LIST, fundBillListStr);
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
