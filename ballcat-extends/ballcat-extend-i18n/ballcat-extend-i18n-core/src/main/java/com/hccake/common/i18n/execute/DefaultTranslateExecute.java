package com.hccake.common.i18n.execute;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.hccake.common.i18n.annotation.I18nField;
import com.hccake.common.i18n.executor.ExecutorWrapper;
import com.hccake.common.i18n.handler.TranslateHandler;
import com.hccake.common.i18n.handler.TranslateHandlerHolder;
import com.hccake.common.i18n.model.I18nValueItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 翻译执行器
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultTranslateExecute extends AbstractTranslateExecute {

	private final ExecutorWrapper executorWrapper;

	@Override
	public String translateText(String businessCode, String code) {
		String languageName = LocaleContextHolder.getLocale().toString();
		return translateText(businessCode, code, languageName, null, code);
	}

	@Override
	public String translateText(String businessCode, String code, String languageName) {
		return translateText(businessCode, code, languageName, null, code);
	}

	@Override
	public String translateText(String businessCode, String code, Map<String, String> params) {
		String languageName = LocaleContextHolder.getLocale().toString();
		return translateText(businessCode, code, languageName, params, code);
	}

	@Override
	public String translateText(String businessCode, String code, String languageName,
			Map<String, String> paramValues) {
		return translateText(businessCode, code, languageName, paramValues, code);
	}

	public String translateText(String businessCode, String code, String languageName, Map<String, String> paramValues,
			String defaultValue) {
		I18nValueItem i18nValueItem = executorWrapper.selectLocaleLanguage(businessCode, code, languageName);
		if (i18nValueItem == null) {
			return code;
		}
		Integer type = i18nValueItem.getType();
		String tplValue = i18nValueItem.getTplValue();
		TranslateHandler translateHandler = TranslateHandlerHolder.getTranslateHandler(type);
		Assert.notNull(translateHandler, "translateHandler can not be Null");
		String resultValue = translateHandler.translateText(tplValue, paramValues);
		return StrUtil.isNotEmpty(resultValue) ? resultValue : defaultValue;
	}

	@Override
	public <T extends Collection> void processObjects(T sources, String language, Map<String, String> params) {
		if (CollectionUtil.isEmpty(sources)) {
			return;
		}
		for (Object source : sources) {
			processObject(source, language, params);
		}
	}

	@Override
	public <T extends Collection> void processObjects(T sources, String language) {
		this.processObjects(sources, language, null);
	}

	@Override
	public void processObject(Object source, String language) {
		this.processObject(source, language, null);
	}

	@Override
	public void processObject(Object source, String language, Map<String, String> params) {
		if (source == null) {
			return;
		}
		Class<?> sourceClass = source.getClass();
		// 若为基本类型 或string类中直接跳过
		if (isBaseTypeOrString(sourceClass)) {
			return;
		}
		for (Field field : sourceClass.getDeclaredFields()) {
			Class<?> fieldType = field.getType();
			// 若为排除类型直接跳过
			if (isExcludeFieldType(fieldType)) {
				continue;
			}
			if (List.class.isAssignableFrom(fieldType)) {
				// 防止实体对象里面防止list属性
				List<Object> elementsList = getFieldValue(field, source);
				if (CollectionUtil.isEmpty(elementsList)) {
					continue;
				}
				processObjects(elementsList, language);
				continue;
			}
			else if (Set.class.isAssignableFrom(fieldType)) {
				Set elementSet = getFieldValue(field, source);
				if (CollectionUtil.isEmpty(elementSet)) {
					continue;
				}
				processObjects(new ArrayList<>(elementSet), language);
				continue;
			}
			// 若不存在国际化注解 直接跳过
			if (!field.isAnnotationPresent(I18nField.class)) {
				continue;
			}
			// 设置字段为可进入
			String fieldValue = getFieldValue(field, source);
			// 若字段值为空 则直接跳过
			if (StrUtil.isEmpty(fieldValue)) {
				continue;
			}
			I18nField annotation = field.getAnnotation(I18nField.class);
			String[] rangeValue = annotation.rangeValue();
			String defaultValue = annotation.defaultValue();
			// rangeValue 不为空 并且当前元素 不在范围值内 直接跳过
			if (ArrayUtil.isNotEmpty(rangeValue) && !ArrayUtil.contains(rangeValue, fieldValue)) {
				continue;
			}
			String businessCode = annotation.businessCode();
			I18nValueItem i18nValueItem = executorWrapper.selectLocaleLanguage(businessCode, fieldValue, language);
			if (i18nValueItem == null) {
				continue;
			}
			String tplValue = i18nValueItem.getTplValue();
			Integer type = i18nValueItem.getType();
			TranslateHandler translateHandler = TranslateHandlerHolder.getTranslateHandler(type);
			if (translateHandler == null) {
				continue;
			}
			String afterValue = translateHandler.translateText(tplValue, params);
			if (StrUtil.isEmpty(afterValue)) {
				if (StrUtil.isEmpty(defaultValue)) {
					continue;
				}
				afterValue = defaultValue;
			}
			// 设置进字段
			setFieldValue(field, source, afterValue);
		}
	}

	/**
	 * 判断是否为基础类型 或String 类型
	 * @param sourceClass
	 * @return
	 */
	private boolean isBaseTypeOrString(Class<?> sourceClass) {
		return ClassUtil.isBasicType(sourceClass) || String.class.isAssignableFrom(sourceClass);
	}

	private <T> T getFieldValue(Field field, Object obj) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			return (T) field.get(obj);
		}
		catch (IllegalAccessException e) {
			log.error("字段值获取失败", e);
		}
		return null;
	}

	/**
	 * 设置字段值
	 * @param field 字段
	 * @param obj 对象
	 * @param val 新值
	 */
	private void setFieldValue(Field field, Object obj, String val) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			field.set(obj, val);
		}
		catch (IllegalAccessException e) {
			log.error("国际化处理结果回填失败", e);
		}
	}

	/**
	 * 是否为排除字段类型
	 * @param clazz string List map 保留
	 * @return true 排除 false 进行翻译
	 */
	public boolean isExcludeFieldType(Class clazz) {
		if (String.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)
				|| Set.class.isAssignableFrom(clazz)) {
			return false;
		}
		return true;
	}

}
