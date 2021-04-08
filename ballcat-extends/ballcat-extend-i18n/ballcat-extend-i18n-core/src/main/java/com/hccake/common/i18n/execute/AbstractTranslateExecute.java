package com.hccake.common.i18n.execute;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;

/**
 * abstract translate execute
 *
 * @author Yakir
 */
public abstract class AbstractTranslateExecute implements TranslateExecute {

	@SuppressWarnings("unchecked")
	@Override
	public void translateObject(Object source) {
		if (source == null) {
			return;
		}
		// 获取本地语言环境
		Locale locale = LocaleContextHolder.getLocale();
		String language = locale.toString();
		if (source instanceof List) {
			processObjects((List<Object>) source, language);
		}
		else if (source instanceof Set) {
			processObjects((Set) source, language);
		}
		else {
			processObject(source, language);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void translateObject(Object source, Map<String, String> params) {
		if (source == null) {
			return;
		}
		// 获取本地语言环境
		Locale locale = LocaleContextHolder.getLocale();
		String language = locale.toString();
		if (source instanceof List) {
			processObjects((List<Object>) source, language, params);
		}
		else if (source instanceof Set) {
			processObjects((Set<Object>) source, language, params);
		}
		else {
			processObject(source, language, params);
		}
	}

	/**
	 * 处理对象 不带参数的
	 * @param object 处理数据对象
	 * @param language 语言环境
	 */
	public abstract void processObject(Object object, String language);

	/**
	 * 处理对象 不带参数
	 * @param sources 元对象集合
	 * @param language 语言环境
	 */
	public abstract <T extends Collection> void processObjects(T sources, String language);

	/**
	 * 处理对象带参数 一批对象公用一份参数
	 * @param sources 元对象集合
	 * @param language 语言环境
	 * @param params 参数
	 */
	public abstract <T extends Collection> void processObjects(T sources, String language, Map<String, String> params);

	/**
	 * 处理对象单个 带参数
	 * @param source 元对象
	 * @param language 语言环境
	 * @param params 参数
	 */
	public abstract void processObject(Object source, String language, Map<String, String> params);

}
