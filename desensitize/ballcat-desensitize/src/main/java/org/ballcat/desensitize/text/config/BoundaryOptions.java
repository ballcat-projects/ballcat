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

package org.ballcat.desensitize.text.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 匹配/处理选项：窗口大小与分隔符/包裹符集合。 作为 POJO 以便 Spring Boot 直接绑定。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
@Accessors(chain = true)
public class BoundaryOptions {

	/**
	 * 值窗口大小。避免值匹配范围过大，导致性能下降。
	 */
	private int windowSize = 256;

	// 是否跳过空白符
	private boolean skipWhitespace = true;

	// 默认空白符集合
	private char[] whitespaceChars = new char[] { ' ', '\t', '\r', '\n', '\f', '\u00A0', '\u3000' };

	// 默认噪声字符集合
	private char[] noiseChars = new char[] { '=', ':', '：', '-', '→', '"', '\'', '[', ']', '【', '】', '{', '}', '(', ')',
			'<', '>', ',', ';', '|', '#', '是' };

	public BoundaryOptions() {
	}

	public static BoundaryOptions defaults() {
		return new BoundaryOptions();
	}

}
