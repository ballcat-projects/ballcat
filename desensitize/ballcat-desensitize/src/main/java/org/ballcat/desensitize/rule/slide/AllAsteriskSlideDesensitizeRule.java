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
 * 全部字符替换为 *。
 *
 * @author Hccake
 */
public class AllAsteriskSlideDesensitizeRule implements SlideDesensitizeRule {

	@Override
	public int leftPlainTextLen() {
		return 0;
	}

	@Override
	public int rightPlainTextLen() {
		return 0;
	}

	@Override
	public String maskString() {
		return "*";
	}

	@Override
	public boolean reverse() {
		return false;
	}

}
