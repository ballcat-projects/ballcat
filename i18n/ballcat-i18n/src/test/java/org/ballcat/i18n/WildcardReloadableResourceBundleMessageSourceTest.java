package org.ballcat.i18n;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardReloadableResourceBundleMessageSourceTest {

	ReloadableResourceBundleMessageSource messageSource(boolean fallbackToSystemLocale) {
		ReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();

		// Specify the location of the properties file
		messageSource.setBasename("classpath*:ballcat-*");
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