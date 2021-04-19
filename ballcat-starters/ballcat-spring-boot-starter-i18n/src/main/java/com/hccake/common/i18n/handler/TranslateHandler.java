package com.hccake.common.i18n.handler;

import java.util.Map;

/**
 * 翻译处理器 具体执行处理逻辑
 *
 * @author Yakir
 */
public interface TranslateHandler {

	/**
	 * 翻译文本
	 * @param originalText 原始文本
	 * @param paramValues 参数值
	 * @return 翻译后的值
	 */
	String translateText(String originalText, Map<String, String> paramValues);

}
