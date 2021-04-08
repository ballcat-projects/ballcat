package com.hccake.common.i18n.executor;

import com.hccake.common.i18n.I18nProperties;
import com.hccake.common.i18n.model.I18nItem;
import com.hccake.common.i18n.model.I18nValueItem;
import lombok.RequiredArgsConstructor;

/**
 * 执行者包装类 主要负责数据的处理转换
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class ExecutorWrapper {

	private final I18nProperties i18nProperties;

	private final Executor executor;

	/**
	 * 获取系统名
	 * @return
	 */
	private String getSystemName() {
		return i18nProperties.getSystemName();
	}

	/**
	 * 查询区域项 语言
	 * @param businessCode
	 * @param code
	 * @param language
	 * @return {@link I18nValueItem} 对应处理后的国际化值
	 */
	public I18nValueItem selectLocaleLanguage(String businessCode, String code, String language) {
		I18nItem i18nItem = executor.selectOne(getSystemName(), businessCode, code, language);
		if (i18nItem == null) {
			return null;
		}
		return convertI18nValueItem(i18nItem);
	}

	private I18nValueItem convertI18nValueItem(I18nItem e) {
		return new I18nValueItem().setTplValue(e.getValue()).setType(e.getType());
	}

}
