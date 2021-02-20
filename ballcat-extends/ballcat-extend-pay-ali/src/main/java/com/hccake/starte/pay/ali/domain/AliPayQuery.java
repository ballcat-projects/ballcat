package com.hccake.starte.pay.ali.domain;

import cn.hutool.core.util.StrUtil;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.hccake.starte.pay.ali.enums.TradeStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

import static com.hccake.starte.pay.ali.constants.AliPayConstant.CODE_SUCCESS;

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
			query.setStatus(TradeStatus.of(raw.getTradeStatus()));
		}
		// 异常
		else {
			query.setStatus(TradeStatus.ERROR);
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
	private TradeStatus status;

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
