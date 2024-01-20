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

package org.ballcat.dingtalk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钉钉消息类型
 *
 * @author lingting 2020/6/10 21:29
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

	/**
	 * 消息值 消息说明
	 */
	TEXT("text", "文本"), LINK("link", "链接"), MARKDOWN("markdown", "markdown"),
	ACTION_CARD("actionCard", "跳转 actionCard 类型"),;

	private final String val;

	private final String desc;

}
