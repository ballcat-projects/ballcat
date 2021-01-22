package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【身份证号】年月日脱敏，前6后4不脱敏 eg. 655356*******1234
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class IDCardNODesensitizationHandler extends AbstractCenterDesensitizationHandler {

	public IDCardNODesensitizationHandler() {
		super(DesensitizationTypeConstant.ID_CARD_NO, 6, 4);
	}

}
