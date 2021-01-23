package com.hccake.ballcat.common.core.desensite.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum RegexDesensitizationTypeEnum {

	/**
	 * 自定义类型
	 */
	CUSTOM("^[\\s\\S]*$", "******"),

	/**
	 * 【邮箱】脱敏，保留邮箱第一个字符和'@'之后的原文显示，中间的显示为4个* eg. 12@qq.com -> 1****@qq.com
	 */
	EMAIL("(^.)[^@]*(@.*$)", "$1****$2"),

	/**
	 * 【对称密文的密码】脱敏，前3后2，中间替换为 4个 *
	 */
	ENCRYPTED_PASSWORD("(.{3}).*(.{2}$)", "$1****$2");

	/**
	 * 匹配的正则表达式
	 */
	private final String regex;

	/**
	 * 替换规则
	 */
	private final String replacement;

}
