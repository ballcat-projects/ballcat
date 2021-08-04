package com.hccake.ballcat.common.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 国际化信息的提供者，使用者实现此接口，用于从数据库或者缓存中读取数据
 *
 * @author hccake
 */
public class DefautlI18nMessageProvider {

	private static final Map<String, I18nMessage> map = new ConcurrentHashMap<>();
	static {
		I18nMessage i18nMessage = new I18nMessage();
		i18nMessage.setMessage("你好啊");
		i18nMessage.setCode("test");
		i18nMessage.setLanguageTag("zh-CN");
		map.put("test:zh-CN", i18nMessage);

		I18nMessage i18nMessage2 = new I18nMessage();
		i18nMessage2.setMessage("Hello");
		i18nMessage2.setCode("test");
		i18nMessage2.setLanguageTag("en-US");
		map.put("test:en-US", i18nMessage2);
	}

	public I18nMessage getI18nMessage(String code, Locale locale) {
		String languageTag = locale.toLanguageTag();
		return map.get(code + ":" + languageTag);
	}

}
