package com.hccake.common.i18n.handler;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * translate handler holder
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class TranslateHandlerHolder {

	/**
	 * 处理器map元素
	 */
	private static final Map<Class<? extends TranslateHandler>, TranslateHandler> HANDLER_MAP = new HashMap<>();

	/**
	 * 枚举类型type标记 与类型的映射
	 */
	private static final Map<Integer, Class<? extends TranslateHandler>> TYPE_CLASS_MAP = new HashMap<>();

	static {

		TYPE_CLASS_MAP.put(1, SimpleTranslateHandler.class);
		TYPE_CLASS_MAP.put(2, TemplateTranslateHandler.class);

		HANDLER_MAP.put(SimpleTranslateHandler.class, new SimpleTranslateHandler());
		HANDLER_MAP.put(TemplateTranslateHandler.class, new TemplateTranslateHandler());
	}

	/**
	 * 得到处理器
	 * @param clazz TranslateHandler的Class
	 * @return  @{code TranslateHandler实现}
	 */
	public static TranslateHandler getTranslateHandler(Class<? extends TranslateHandler> clazz) {
		return HANDLER_MAP.get(clazz);
	}

	/**
	 * 添加处理器
	 * @param clazz TranslateHandler的Class
	 * @param translateHandler TranslateHandler实现
	 * @return @{code TranslateHandler实现}
	 */
	public static TranslateHandler addTranslateHandler(Class<? extends TranslateHandler> clazz,
			TranslateHandler translateHandler) {
		return HANDLER_MAP.put(clazz, translateHandler);
	}

	/**
	 * 得到处理器 主要拓展 绑定type 与类的映射
	 * @param type type
	 * @return @{code TranslateHandler实现}
	 */
	public static TranslateHandler getTranslateHandler(Integer type) {
		return HANDLER_MAP.get(TYPE_CLASS_MAP.get(type));
	}

	/**
	 * 添加处理器 主要拓展 绑定type 与类的映射
	 * @param type 类型1.明文 2.模板
	 * @param clazz TranslateHandler的Class
	 * @param translateHandler TranslateHandler实现
	 * @return @{code TranslateHandler实现}
	 */
	public static TranslateHandler addTranslateHandler(Integer type, Class<? extends TranslateHandler> clazz,
			TranslateHandler translateHandler) {
		TYPE_CLASS_MAP.put(type, clazz);
		return HANDLER_MAP.put(clazz, translateHandler);
	}

}
