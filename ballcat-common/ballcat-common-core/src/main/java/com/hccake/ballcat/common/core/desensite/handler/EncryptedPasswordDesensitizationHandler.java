package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【加密后的密码脱敏】
 *
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public class EncryptedPasswordDesensitizationHandler extends AbstractCenterDesensitizationHandler {

	public EncryptedPasswordDesensitizationHandler() {
		super(DesensitizationTypeConstant.ENCRYPTED_PASSWORD, 3, 2);
	}

}
