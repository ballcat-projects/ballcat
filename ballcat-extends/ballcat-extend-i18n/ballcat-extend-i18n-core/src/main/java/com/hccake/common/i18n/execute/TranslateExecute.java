package com.hccake.common.i18n.execute;

import java.util.Map;

public interface TranslateExecute {

	/**
	 * 翻译文本使用默认语言环境
	 * @param businessCode
	 * @param code
	 * @return
	 */
	String translateText(String businessCode, String code);

	/**
	 * 翻译文本 指定语言环境
	 * @param businessCode
	 * @param code
	 * @param language
	 * @return
	 */
	String translateText(String businessCode, String code, String language);

	/**
	 * 翻译文本使用当前语言环境
	 * @param businessCode
	 * @param code
	 * @param params
	 * @return
	 */
	String translateText(String businessCode, String code, Map<String, String> params);

	/**
	 * 翻译文本 指定语言环境
	 * @param businessCode
	 * @param code
	 * @param language
	 * @param params
	 * @return
	 */
	String translateText(String businessCode, String code, String language, Map<String, String> params);

	/**
	 * 翻译对象 直接进行字段值更新
	 * @param source
	 */
	void translateObject(Object source);

	/**
	 * 翻译单个对象 可以指定参数
	 * @param source
	 * @param params
	 */
	void translateObject(Object source, Map<String, String> params);

}
