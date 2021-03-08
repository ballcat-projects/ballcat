package com.hccake.starte.pay.ali.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.starte.pay.ali.AliPay;
import com.hccake.starte.pay.ali.enums.TradeStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/1/26 13:31
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
		return JsonUtils.toObj(JsonUtils.toJson(map), AliPayCallback.class).setRaw(callbackParams);
	}

	@SneakyThrows
	public boolean checkSign(AliPay aliPay) {
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
