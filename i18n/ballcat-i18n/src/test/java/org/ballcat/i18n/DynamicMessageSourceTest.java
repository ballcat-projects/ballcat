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
import org.springframework.context.NoSuchMessageException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicMessageSourceTest {

	DynamicMessageSource messageSource() {
		I18nMessageProvider i18nMessageProvider = new DefaultI18nMessageProvider();
		return new DynamicMessageSource(i18nMessageProvider);
	}

	@Test
	void testUseCodeAsDefaultMessage() {
		DynamicMessageSource messageSource = messageSource();

		String i18nCode = "i18n:test";

		String enMessage = messageSource.getMessage(i18nCode, null, Locale.US);
		assertEquals("Hello", enMessage);

		String cnMessage = messageSource.getMessage(i18nCode, null, Locale.CHINA);
		assertEquals("你好啊", cnMessage);

		// 没有配置语言查找不到就会抛出异常
		assertThrows(NoSuchMessageException.class, () -> messageSource.getMessage(i18nCode, null, Locale.JAPAN));

		// 修改：当找不到语言对应配置时，使用 code 作为默认值
		messageSource.setUseCodeAsDefaultMessage(true);

		String jpMessage = messageSource.getMessage(i18nCode, null, Locale.JAPAN);
		assertEquals(i18nCode, jpMessage);
	}

}
