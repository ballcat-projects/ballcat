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

package org.ballcat.desensitize.text;

import lombok.Data;
import org.ballcat.desensitize.text.config.BoundaryOptions;

/**
 * 边界定位器，用于定位 value 起点、结束位置。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class BoundaryLocator {

	private final BoundaryOptions options;

	BoundaryLocator(BoundaryOptions options) {
		this.options = options == null ? BoundaryOptions.defaults() : options;
	}

	public Boundary locate(CharSequence text, int prefixEnd) {
		// 1) 从 prefixEnd 向右：跳过 whitespaceChars 与 noiseChars（可配置）
		// 2) 若遇到包裹符起点（如 " ' [ 【 ( { <），记录预期闭合符
		// 3) 计算 start；若无合法 start，返回 null
		// 4) 计算 endExclusive：
		// - 若包裹符闭合符可在 window 内找到，endExclusive = closePos + 1
		// - 否则 endExclusive = min(start + options.windowSize, text.length())
		// - 可扩展：若配置“值中断字符集”（如空白/分隔符）启用，则遇到即提前截断

		int valueStart = locateValueStart(text, prefixEnd);
		int endExclusive = Math.min(valueStart + this.options.getWindowSize(), text.length());
		return new Boundary(valueStart, endExclusive);
	}

	private int locateValueStart(CharSequence text, int fromIndex) {
		if (text == null) {
			return -1;
		}
		int len = text.length();
		int i = Math.max(0, fromIndex);

		while (i < len) {
			char c = text.charAt(i);
			if (isSkippable(c)) {
				i++;
				continue;
			}
			return i; // 第一个非噪声字符就是 value 起点
		}
		return -1;
	}

	private boolean isSkippable(char c) {
		// 跳过空白符
		if (this.options.isSkipWhitespace()) {
			for (char w : this.options.getWhitespaceChars()) {
				if (c == w) {
					return true;
				}
			}
		}
		// 跳过噪声符
		for (char n : this.options.getNoiseChars()) {
			if (c == n) {
				return true;
			}
		}
		return false;
	}

	@Data
	public static final class Boundary {

		private final int start;

		private final int endExclusive;

		public Boundary(int start, int endExclusive) {
			this.start = start;
			this.endExclusive = endExclusive;
		}

	}

}
