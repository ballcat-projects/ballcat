package com.hccake.starte.pay.ali.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 交易状态
 */
public enum TradeStatus {

	/**
	 * 成功
	 */
	SUCCESS,
	/**
	 * 未支付
	 */
	WAIT,
	/**
	 * 未付款交易超时关闭，或支付完成后全额退款
	 */
	CLOSED,
	/**
	 * 交易结束，不可退款
	 */
	FINISHED,
	/**
	 * 异常. 具体信息查询 subCode和subMsg
	 */
	ERROR,

	;

	@JsonCreator
	public static TradeStatus of(String status) {
		switch (status) {
			case "WAIT_BUYER_PAY":
				return WAIT;
			case "TRADE_CLOSED":
				return CLOSED;
			case "TRADE_SUCCESS":
				return SUCCESS;
			case "TRADE_FINISHED":
				return FINISHED;
			default:
				return ERROR;
		}
	}

}
