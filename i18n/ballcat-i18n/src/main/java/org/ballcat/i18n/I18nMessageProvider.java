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

import java.util.Locale;

/**
 * 国际化信息的提供者，使用者实现此接口，用于从数据库或者缓存中读取数据
 *
 * @author hccake
 */
public interface I18nMessageProvider {

	/**
	 * 获取 I18nMessage 对象
	 * @param code 国际化唯一标识
	 * @param locale 语言
	 * @return 国际化消息
	 */
	I18nMessage getI18nMessage(String code, Locale locale);

}
