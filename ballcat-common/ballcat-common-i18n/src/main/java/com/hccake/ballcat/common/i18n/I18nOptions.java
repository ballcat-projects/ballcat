package com.hccake.ballcat.common.i18n;

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
