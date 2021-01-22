package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationTypeConstant;

/**
 * 【邮箱】脱敏，保留邮箱第一个字符和'@'之后的原文显示，中间的显示为4个* eg. 12@qq.com -> 1****@qq.com
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
public class EmailDesensitizationHandler extends AbstractRegexDesensitizationHandler {

	public EmailDesensitizationHandler() {
		super(DesensitizationTypeConstant.EMAIL, "(^\\w)[^@]*(@.*$)", "$1****$2");
	}

}
