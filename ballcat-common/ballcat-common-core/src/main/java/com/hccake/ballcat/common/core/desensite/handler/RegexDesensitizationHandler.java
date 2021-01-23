package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.enums.RegexDesensitizationTypeEnum;

/**
 * 正则替换脱敏处理器，使用正则匹配替换处理原数据
 *
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public class RegexDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 正则脱敏处理
	 * @param origin 原文
	 * @param regex 正则匹配规则
	 * @param replacement 替换模板
	 * @return 脱敏后的字符串
	 */
	public String handle(String origin, String regex, String replacement) {
		return origin.replaceAll(regex, replacement);
	}

	/**
	 * 正则脱敏处理
	 * @param origin 原文
	 * @param typeEnum 正则脱敏枚举类型
	 * @return 脱敏后的字符串
	 */
	public String handle(String origin, RegexDesensitizationTypeEnum typeEnum) {
		return origin.replaceAll(typeEnum.getRegex(), typeEnum.getReplacement());
	}

}
