package com.hccake.starte.pay.wx.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author lingting 2021/2/25 15:35
 */
public enum TradeState {

	/**
	 * 支付成功
	 */
	SUCCESS,
	/**
	 * 转入退款
	 */
	REFUND,
	/**
	 * 未支付
	 */
	NOTPAY,
	/**
	 * 已关闭
	 */
	CLOSED,
	/**
	 * 已撤销(刷卡支付)
	 */
	REVOKED,
	/**
	 * 用户支付中
	 */
	USERPAYING,
	/**
	 * 支付失败(其他原因，如银行返回失败)
	 */
	PAYERROR,
	/**
	 * 已接收，等待扣款
	 */
	ACCEPT,
	/**
	 * 异常
	 */
	ERROR,

	;

	@JsonCreator
	public static TradeState of(String status) {
		switch (status) {
		case "SUCCESS":
			return SUCCESS;
		case "REFUND":
			return REFUND;
		case "NOTPAY":
			return NOTPAY;
		case "CLOSED":
			return CLOSED;
		case "REVOKED":
			return REVOKED;
		case "USERPAYING":
			return USERPAYING;
		case "PAYERROR":
			return PAYERROR;
		case "ACCEPT":
			return ACCEPT;
		default:
			return ERROR;
		}
	}

}
