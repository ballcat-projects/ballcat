package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【银行卡号】, 前6位和后4位不脱敏，中间脱敏 eg. 330150******1234
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class BankCardNODesensitizationHandler extends AbstractCenterDesensitizationHandler {

	public BankCardNODesensitizationHandler() {
		super(DesensitizationTypeConstant.BANK_CARD_NO, 6, 4);
	}

}
