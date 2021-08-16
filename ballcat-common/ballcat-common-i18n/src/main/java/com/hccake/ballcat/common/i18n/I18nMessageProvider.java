package com.hccake.ballcat.common.i18n;

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
