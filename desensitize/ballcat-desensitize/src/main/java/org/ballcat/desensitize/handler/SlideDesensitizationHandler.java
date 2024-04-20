/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.desensitize.handler;

import org.ballcat.desensitize.rule.slide.SlideDesensitizeRule;

/**
 * 滑动脱敏处理器，根据左右边界值滑动左右指针，中间处脱敏
 *
 * @author Hccake 2021/1/23
 *
 */
public class SlideDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 滑动脱敏
	 * @param origin 原文
	 * @param leftPlainTextLen 处理完后左边的明文数
	 * @param rightPlainTextLen 处理完后右边的明文数
	 * @return 脱敏后的字符串
	 */
	public String mask(String origin, int leftPlainTextLen, int rightPlainTextLen) {
		return this.mask(origin, leftPlainTextLen, rightPlainTextLen, false);
	}

	/**
	 * 滑动脱敏
	 * @param origin 原文
	 * @param leftPlainTextLen 处理完后左边的明文数
	 * @param rightPlainTextLen 处理完后右边的明文数
	 * @param reverse 是否反转
	 * @return 脱敏后的字符串
	 */
	public String mask(String origin, int leftPlainTextLen, int rightPlainTextLen, boolean reverse) {
		return this.mask(origin, leftPlainTextLen, rightPlainTextLen, "*", reverse);
	}

	/**
	 * 滑动脱敏
	 * @param origin 原文
	 * @param leftPlainTextLen 处理完后左边的明文数
	 * @param rightPlainTextLen 处理完后右边的明文数
	 * @param maskString 原文窗口内每个字符被替换后的字符串
	 * @return 脱敏后的字符串
	 */
	public String mask(String origin, int leftPlainTextLen, int rightPlainTextLen, String maskString) {
		return this.mask(origin, leftPlainTextLen, rightPlainTextLen, maskString, false);
	}

	/**
	 * 滑动脱敏
	 * @param origin 原文
	 * @param leftPlainTextLen 处理完后左边的明文数
	 * @param rightPlainTextLen 处理完后右边的明文数
	 * @param maskString 原文窗口内每个字符被替换后的字符串
	 * @param reverse 是否反转
	 * @return 脱敏后的字符串
	 */
	public String mask(String origin, int leftPlainTextLen, int rightPlainTextLen, String maskString, boolean reverse) {
		if (origin == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		char[] chars = origin.toCharArray();
		int length = chars.length;
		for (int i = 0; i < length; i++) {
			// 明文位内则明文显示
			if (i < leftPlainTextLen || i > (length - rightPlainTextLen - 1)) {
				sb.append(reverse ? maskString : chars[i]);
			}
			else {
				sb.append(reverse ? chars[i] : maskString);
			}
		}
		return sb.toString();
	}

	/**
	 * 根据指定枚举类型进行滑动脱敏
	 * @param value 原文
	 * @param slideDesensitizeRule 滑动脱敏规则
	 * @return 脱敏后的字符串
	 */
	public String mask(String value, SlideDesensitizeRule slideDesensitizeRule) {
		return this.mask(value, slideDesensitizeRule, false);
	}

	/**
	 * 根据指定枚举类型进行滑动脱敏
	 * @param value 原文
	 * @param slideDesensitizeRule 滑动脱敏规则
	 * @param reverse 是否反转
	 * @return 脱敏后的字符串
	 */
	public String mask(String value, SlideDesensitizeRule slideDesensitizeRule, boolean reverse) {
		return this.mask(value, slideDesensitizeRule.leftPlainTextLen(), slideDesensitizeRule.rightPlainTextLen(),
				slideDesensitizeRule.maskString(), reverse);
	}

}
