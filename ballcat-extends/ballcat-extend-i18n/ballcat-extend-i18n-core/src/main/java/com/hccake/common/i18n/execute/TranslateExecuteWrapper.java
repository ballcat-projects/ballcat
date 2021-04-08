package com.hccake.common.i18n.execute;

import java.util.Map;

/**
 * 翻译执行包装器
 *
 * @author Yakir
 */
public class TranslateExecuteWrapper {

	private static TranslateExecute translateExecute;

	public static String translateText(String businessCode, String code) {
		return translateExecute.translateText(businessCode, code);
	}

	public static String translateText(String businessCode, String code, String language) {
		return translateExecute.translateText(businessCode, code, language);
	}

	public static String translateText(String businessCode, String code, Map<String, String> params) {
		return translateExecute.translateText(businessCode, code, params);
	}

	public static String translateText(String businessCode, String code, String language, Map<String, String> params) {
		return translateExecute.translateText(businessCode, code, language, params);
	}

	public static void translateObject(Object source) {
		translateExecute.translateObject(source);
	}

	public static void translateObject(Object source, Map<String, String> params) {
		translateExecute.translateObject(source, params);
	}

	public void setTranslateExecute(TranslateExecute translateExecute) {
		TranslateExecuteWrapper.translateExecute = translateExecute;
	}

}
