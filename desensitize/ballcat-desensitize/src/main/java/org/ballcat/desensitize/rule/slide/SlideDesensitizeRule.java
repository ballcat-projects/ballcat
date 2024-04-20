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

package org.ballcat.desensitize.rule.slide;

/**
 * 滑动脱敏规则。
 *
 * @author Hccake
 */
public interface SlideDesensitizeRule {

	/**
	 * 左边的明文数
	 */
	int leftPlainTextLen();

	/**
	 * 右边的明文数
	 */
	int rightPlainTextLen();

	/**
	 * 剩余部分字符逐个替换的字符串
	 */
	String maskString();

	/**
	 * 反转规则，修改为外部脱敏，内部明文
	 */
	boolean reverse();

}
