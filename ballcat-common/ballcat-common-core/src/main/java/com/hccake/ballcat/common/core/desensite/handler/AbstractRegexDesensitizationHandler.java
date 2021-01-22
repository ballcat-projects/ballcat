package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationHandler;
import lombok.Getter;

/**
 * 正则替换脱敏处理器类型
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Getter
public abstract class AbstractRegexDesensitizationHandler implements DesensitizationHandler {

	private final String type;

	private final String regex;

	private final String replacement;

	public AbstractRegexDesensitizationHandler(String type, String regex, String replacement) {
		this.type = type;
		this.regex = regex;
		this.replacement = replacement;
	}

	/**
	 * 脱敏处理
	 * @param origin 原始字符串
	 * @return 脱敏处理后的字符串
	 */
	@Override
	public String handle(String origin) {
		return origin.replaceAll(regex, replacement);
	}

}
