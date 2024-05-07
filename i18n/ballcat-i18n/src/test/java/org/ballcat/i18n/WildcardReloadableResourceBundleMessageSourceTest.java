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

class WildcardReloadableResourceBundleMessageSourceTest {

	ReloadableResourceBundleMessageSource messageSource(boolean fallbackToSystemLocale) {
		ReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();

		// Specify the location of the properties file
		messageSource.setBasename("org.ballcat.**.messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(fallbackToSystemLocale);
		return messageSource;
	}

	@Test
	void testFallbackDefaultBundle() {
		ReloadableResourceBundleMessageSource messageSource = messageSource(true);
		messageSource.setDefaultLocale(Locale.CHINA);

		String i18nCode = "i18nMessage.code";

		String enMessage = messageSource.getMessage(i18nCode, null, Locale.US);
		assertEquals("Code", enMessage);

		String cnMessage = messageSource.getMessage(i18nCode, null, Locale.CHINA);
		assertEquals("国际化标识", cnMessage);

		// 没有配置的应该回退默认语言的配置
		String jpMessage = messageSource.getMessage(i18nCode, null, Locale.JAPAN);
		assertEquals("国际化标识", jpMessage);
	}

	@Test
	void testNotFallbackDefaultBundle() {
		MessageSource messageSource = messageSource(false);

		String i18nCode = "i18nMessage.code";

		String enMessage = messageSource.getMessage(i18nCode, null, Locale.US);
		assertEquals("Code", enMessage);

		String cnMessage = messageSource.getMessage(i18nCode, null, Locale.CHINA);
		assertEquals("国际化标识", cnMessage);

		// 没有配置的应该回退默认配置
		String jpMessage = messageSource.getMessage(i18nCode, null, Locale.JAPAN);
		assertEquals("Code", jpMessage);
	}

}
