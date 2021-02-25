package com.hccake.starte.pay.wx.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hccake.ballcat.common.core.util.JacksonUtils;
import com.hccake.starte.pay.wx.WxPay;
import com.hccake.starte.pay.wx.enums.ResponseCode;
import com.hccake.starte.pay.wx.enums.TradeType;
import java.math.BigInteger;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/2/25 15:43
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxPayCallback {

	@JsonProperty("transaction_id")
	private String transactionId;

	@JsonProperty("nonce_str")
	private String nonceStr;

	@JsonProperty("bank_type")
	private String bankType;

	@JsonProperty("openid")
	private String openid;

	@JsonProperty("sign")
	private String sign;

	@JsonProperty("fee_type")
	private String feeType;

	@JsonProperty("mch_id")
	private String mchId;

	@JsonProperty("cash_fee")
	private BigInteger cashFee;

	@JsonProperty("out_trade_no")
	private String outTradeNo;

	@JsonProperty("appid")
	private String appid;

	@JsonProperty("total_fee")
	private BigInteger totalFee;

	@JsonProperty("trade_type")
	private TradeType tradeType;

	@JsonProperty("result_code")
	private ResponseCode resultCode;

	@JsonProperty("time_end")
	private String timeEnd;

	@JsonProperty("is_subscribe")
	private String isSubscribe;

	@JsonProperty("return_code")
	private ResponseCode returnCode;

	public static WxPayCallback of(Map<String, String> res) {
		return JacksonUtils.toObj(JacksonUtils.toJson(res), WxPayCallback.class).setRaw(res);
	}

	/**
	 * 返回的原始数据
	 */
	private Map<String, String> raw;

	/**
	 * 验签
	 * @param wxPay 微信支付信息
	 * @return boolean
	 * @author lingting 2021-02-25 16:04
	 */
	public boolean checkSign(WxPay wxPay) {
		return wxPay.checkSign(this);
	}

}
