package com.hccake.starte.pay.ali.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.starte.pay.ali.enums.TradeStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2021/1/26 13:31
 */
@NoArgsConstructor
@Data
public class AliPayCallback {

	/**
	 * 解析回调参数
	 * @param callbackParams 所有回调参数
	 * @return com.hccake.starte.pay.ali.domain.AliPayCallback
	 * @author lingting 2021-01-26 14:39
	 */
	public static AliPayCallback of(Map<String, String> callbackParams) {
		Map<String, Object> map = new HashMap<>(callbackParams);
		String fundBillListStr = callbackParams.get("fund_bill_list").replaceAll("&quot;", "\"");
		map.put("fund_bill_list", JsonUtils.toObj(fundBillListStr, List.class));
		// 覆盖原值
		callbackParams.put("fund_bill_list", fundBillListStr);
		return JsonUtils.toObj(JsonUtils.toJson(map), AliPayCallback.class);
	}

	@JsonProperty("gmt_create")
	private String gmtCreate;

	@JsonProperty("charset")
	private String charset;

	@JsonProperty("seller_email")
	private String sellerEmail;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("sign")
	private String sign;

	@JsonProperty("buyer_id")
	private String buyerId;

	@JsonProperty("invoice_amount")
	private BigDecimal invoiceAmount;

	@JsonProperty("notify_id")
	private String notifyId;

	@JsonProperty("fund_bill_list")
	private List<FundBill> fundBillList;

	@JsonProperty("notify_type")
	private String notifyType;

	@JsonProperty("trade_status")
	private TradeStatus tradeStatus;

	@JsonProperty("receipt_amount")
	private BigDecimal receiptAmount;

	@JsonProperty("app_id")
	private String appId;

	@JsonProperty("buyer_pay_amount")
	private BigDecimal buyerPayAmount;

	@JsonProperty("sign_type")
	private String signType;

	@JsonProperty("seller_id")
	private String sellerId;

	@JsonProperty("gmt_payment")
	private String gmtPayment;

	@JsonProperty("notify_time")
	private String notifyTime;

	@JsonProperty("version")
	private String version;

	@JsonProperty("out_trade_no")
	private String outTradeNo;

	@JsonProperty("total_amount")
	private BigDecimal totalAmount;

	@JsonProperty("trade_no")
	private String tradeNo;

	@JsonProperty("auth_app_id")
	private String authAppId;

	@JsonProperty("buyer_logon_id")
	private String buyerLogonId;

	@JsonProperty("point_amount")
	private BigDecimal pointAmount;

	@Data
	public static class FundBill {

		private BigDecimal amount;

		private String fundChannel;

	}

}
