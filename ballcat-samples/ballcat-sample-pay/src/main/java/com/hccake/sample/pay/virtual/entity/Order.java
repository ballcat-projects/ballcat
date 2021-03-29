package com.hccake.sample.pay.virtual.entity;

import com.hccake.sample.pay.virtual.enums.Contract;
import com.hccake.sample.pay.virtual.enums.Status;
import com.hccake.extend.pay.viratual.VerifyObj;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 充值订单, 实现 {@link VerifyObj}
 *
 * @author lingting 2021/1/5 14:26
 */
@Data
@Accessors(chain = true)
public class Order implements VerifyObj {

	/**
	 * 订单号
	 */
	private Long sn;

	/**
	 * 交易hash
	 */
	private String hash;

	/**
	 * 收款地址
	 */
	private String address;

	/**
	 * 收款货币类型
	 */
	private Contract contract;

	/**
	 * 充值状态
	 */
	private Status status;

	/**
	 * 充值金额
	 */
	private BigDecimal value;

	/**
	 * 订单描述, 一般用于解释失败订单的失败原因
	 */
	private String desc;

	private LocalDateTime createTime;

}
