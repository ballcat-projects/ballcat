package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【手机号】，某些国家手机号位数短，所以不做前三后四，使用前三后二
 *
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public class PhoneNumberDesensitizationHandler extends AbstractCenterDesensitizationHandler {

	public PhoneNumberDesensitizationHandler() {
		super(DesensitizationTypeConstant.PHONE_NUMBER, 3, 2);
	}

}
