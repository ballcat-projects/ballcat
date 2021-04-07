package com.hccake.common.i18n.executor;

import com.hccake.common.i18n.I18nDataProvider;
import com.hccake.common.i18n.model.I18nItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 简单执行器 主要负责数据提取与缓存
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class SimpleExecutor implements Executor {

	private final I18nDataProvider i18nDataProvider;

	@Override
	public I18nItem selectOne(String systemName, String businessCode, String code, String language) {
		return i18nDataProvider.selectOne(systemName, businessCode, code, language);
	}

	@Override
	public List<I18nItem> selectListByCode(String systemName, String businessCode, String code) {
		return i18nDataProvider.selectListByCode(systemName, businessCode, code);
	}

}
