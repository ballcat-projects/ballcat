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

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HierarchicalMessageSourceTest {

	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();
		// Specify the location of the properties file
		messageSource.setBasename("test-*");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);

		I18nMessageProvider i18nMessageProvider = new DefaultI18nMessageProvider();
		DynamicMessageSource dynamicMessageSource = new DynamicMessageSource(i18nMessageProvider);
		dynamicMessageSource.setParentMessageSource(messageSource);

		return dynamicMessageSource;
	}

	@Test
	void testParentMessageSource() {
		MessageSource messageSource = messageSource();

		// ---------- 从 resource bundle 中查找 ---
		String i18nCode = "test.name";

		String enMessage = messageSource.getMessage(i18nCode, null, Locale.US);
		assertEquals("English Name", enMessage);

		String cnMessage = messageSource.getMessage(i18nCode, null, Locale.CHINA);
		assertEquals("中文名称", cnMessage);

		// 没有配置的应该回退默认配置
		String jpMessage = messageSource.getMessage(i18nCode, null, Locale.JAPAN);
		assertEquals("默认名称", jpMessage);

		// ---------- 从 dynamic message source 中查找 ---
		i18nCode = "i18n:test";

		String enMessage2 = messageSource.getMessage(i18nCode, null, Locale.US);
		assertEquals("Hello", enMessage2);

		String cnMessage2 = messageSource.getMessage(i18nCode, null, Locale.CHINA);
		assertEquals("你好啊", cnMessage2);
	}

}
