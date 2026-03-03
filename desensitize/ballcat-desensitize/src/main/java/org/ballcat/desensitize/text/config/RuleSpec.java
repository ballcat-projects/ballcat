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

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ballcat.desensitize.DesensitizeType;

/**
 * 规则配置 POJO（供 Starter 直接绑定）。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
@Accessors(chain = true)
public class RuleSpec {

	/**
	 * 规则名称。
	 */
	private String name;

	/**
	 * 匹配前缀。
	 */
	private List<String> prefixes = new ArrayList<>();

	/**
	 * 匹配值正则。
	 */
	private String valuePattern;

	/**
	 * 匹配值正则是否强制从开头。true 会使用 lookingAt, false 会使用 find.
	 */
	private Boolean matchFromStart = Boolean.TRUE;

	/**
	 * 脱敏类型。
	 */
	private DesensitizeType desensitizeType;

	/**
	 * 正则替换参数。
	 */
	private RegexReplacementParams regex;

	/**
	 * 滑动脱敏参数。
	 */
	private SlideMaskParams slide;

	/**
	 * 简单处理参数。
	 */
	private SimpleHandlerParams simple;

}
