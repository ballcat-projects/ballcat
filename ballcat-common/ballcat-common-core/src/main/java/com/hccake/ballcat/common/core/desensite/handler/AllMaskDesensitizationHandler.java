package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【全部】所有字符全部脱敏，保留原始位数不变 eg. 123456789 -> *********
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class AllMaskDesensitizationHandler extends AbstractCenterDesensitizationHandler {

	public AllMaskDesensitizationHandler() {
		super(DesensitizationTypeConstant.ALL_MASK, 0, 0);
	}

}
