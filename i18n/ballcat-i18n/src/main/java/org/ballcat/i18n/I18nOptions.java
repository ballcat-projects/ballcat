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

package org.ballcat.i18n;

import lombok.Data;

/**
 * @author hccake
 */
@Data
public class I18nOptions {

	/**
	 * 如果没有找到指定 languageTag 的语言配置时，需要回退的 languageTag，不配置则表示不回退
	 */
	private String fallbackLanguageTag = "zh-CN";

	/**
	 * 是否使用消息代码作为默认消息而不是抛出“NoSuchMessageException”。
	 */
	private boolean useCodeAsDefaultMessage = true;

}
