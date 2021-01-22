package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.DesensitizationHandler;
import lombok.Getter;

/**
 * 中间脱敏处理器类型，两边各展示部分明文数据
 *
 * @author Hccake 2021/1/22
 * @version 1.0
 */
@Getter
public abstract class AbstractCenterDesensitizationHandler implements DesensitizationHandler {

	private final String type;

	private final int leftPlainTextLen;

	private final int rightPlainTextLen;

	private final String maskString;

	public AbstractCenterDesensitizationHandler(String type, int leftPlainTextLen, int rightPlainTextLen) {
		this(type, leftPlainTextLen, rightPlainTextLen, "*");
	}

	public AbstractCenterDesensitizationHandler(String type, int leftPlainTextLen, int rightPlainTextLen,
			String maskString) {
		this.type = type;
		this.leftPlainTextLen = leftPlainTextLen;
		this.rightPlainTextLen = rightPlainTextLen;
		this.maskString = maskString;
	}

	/**
	 * 脱敏处理
	 * @param origin 原始字符串
	 * @return 脱敏处理后的字符串
	 */
	@Override
	public String handle(String origin) {
		if (origin == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		char[] chars = origin.toCharArray();
		int length = chars.length;
		for (int i = 0; i < length; i++) {
			// 明文位内则明文显示
			if (i < leftPlainTextLen || i > (length - rightPlainTextLen - 1)) {
				sb.append(chars[i]);
			}
			else {
				sb.append(maskString);
			}
		}
		return sb.toString();
	}

}
