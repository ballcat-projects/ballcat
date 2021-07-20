package com.hccake.common.i18n.executor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.common.i18n.I18nDataProvider;
import com.hccake.common.i18n.I18nProperties;
import com.hccake.common.i18n.cache.CacheService;
import com.hccake.common.i18n.generate.KeyGenerate;
import com.hccake.common.i18n.model.I18nItem;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单执行器 主要负责数据提取与缓存
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class CacheExecutor implements Executor {

	private final I18nDataProvider i18nDataProvider;

	private final I18nProperties i18nProperties;

	private final KeyGenerate keyGenerate;

	private final CacheService cacheService;

	@Override
	public I18nItem selectOne(String systemName, String businessCode, String code, String language) {
		String key = keyGenerate.generateKey(systemName, businessCode, code, language);
		String cacheValue = cacheService.get(key);
		// 若为空值标记 则直接返回
		if (i18nProperties.isNullValue(cacheValue)) {
			return null;
		}
		if (StrUtil.isNotEmpty(cacheValue)) {
			return JsonUtils.toObj(cacheValue, I18nItem.class);
		}
		I18nItem i18nItem = i18nDataProvider.selectOne(systemName, businessCode, code, language);

		cacheService.put(key,
				ObjectUtil.isNotEmpty(i18nItem) ? JsonUtils.toJson(i18nItem) : i18nProperties.getNullValue(),
				i18nProperties.getCache().getExpire());

		return i18nItem;
	}

	@Override
	public List<I18nItem> selectListByCode(String systemName, String businessCode, String code) {
		String key = keyGenerate.generateKey(systemName, businessCode, code);
		String cacheValue = cacheService.get(key);
		// 若为空值标记 则直接返回
		if (i18nProperties.isNullValue(cacheValue)) {
			return new ArrayList<>();
		}

		if (StrUtil.isNotEmpty(cacheValue)) {
			return JsonUtils.toObj(cacheValue, List.class);
		}
		List<I18nItem> i18nItems = i18nDataProvider.selectListByCode(systemName, businessCode, code);

		cacheService.put(key,
				ObjectUtil.isNotEmpty(i18nItems) ? JsonUtils.toJson(i18nItems) : i18nProperties.getNullValue(),
				i18nProperties.getCache().getExpire());

		return i18nItems;
	}

}
