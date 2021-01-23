package com.hccake.ballcat.common.core.desensite.handler;

import com.hccake.ballcat.common.core.desensite.enums.SlideDesensitizationTypeEnum;

/**
 * 滑动脱敏处理器，根据左右边界值滑动左右指针，中间处脱敏
 *
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public class SlideDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 滑动脱敏
	 * @param origin 原文
	 * @param leftPlainTextLen 处理完后左边的明文数
	 * @param rightPlainTextLen 处理完后右边的明文数
	 * @param maskString 原文窗口内每个字符被替换后的字符串
	 * @return 脱敏后的字符串
	 */
	public String handle(String origin, int leftPlainTextLen, int rightPlainTextLen, String maskString) {
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

	/**
	 * 根据指定枚举类型进行滑动脱敏
	 * @param value 原文
	 * @param type 滑动脱敏类型
	 * @return 脱敏后的字符串
	 */
	public String handle(String value, SlideDesensitizationTypeEnum type) {
		return this.handle(value, type.getLeftPlainTextLen(), type.getRightPlainTextLen(), type.getMaskString());
	}

}
