package com.hccake.starte.pay.ali.domain;

import static com.hccake.starte.pay.ali.constants.AliPayConstant.CODE_SUCCESS;

import cn.hutool.core.util.StrUtil;
import com.alipay.api.response.AlipayTradeQueryResponse;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
		if (CODE_SUCCESS.equals(raw.getCode())) {
			// 成功
			query.setStatus(Status.of(raw.getTradeStatus()));
		}
		// 异常
		else {
			query.setStatus(Status.ERROR);
		}

		// 金额
		if (StrUtil.isBlank(raw.getTotalAmount())) {
			query.setAmount(BigDecimal.ZERO);
		}
		else {
			query.setAmount(new BigDecimal(raw.getTotalAmount()));
		}

		// 信息
		query.setCode(raw.getCode()).setMsg(raw.getMsg()).setSubCode(raw.getSubCode()).setSubMsg(raw.getSubMsg());

		// 基础数据
		return query.setTradeNo(raw.getTradeNo()).setSn(raw.getOutTradeNo()).setId(raw.getBuyerLogonId())
				.setUserId(raw.getBuyerUserId()).setUserName(raw.getBuyerUserName())
				.setUserType(raw.getBuyerUserType());
	}

	/**
	 * 原始数据
	 */
	private AlipayTradeQueryResponse raw;

	/**
	 * 订单状态
	 */
	private Status status;

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

	/**
	 * 交易状态
	 */
	public enum Status {

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

		public static Status of(String status) {
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

}
