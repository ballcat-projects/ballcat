package com.hccake.common.i18n;

import com.hccake.common.i18n.model.I18nItem;

import java.util.List;

/**
 * i18n 数据提供 主要负责从数据库中提取数据
 *
 * @author Yakir
 * @since 2021/3/30
 */
public interface I18nDataProvider {

	/**
	 * 指定业务 业务码 语言环境
	 * @param systemName
	 * @param businessCode
	 * @param code
	 * @param language
	 * @return
	 */
	I18nItem selectOne(String systemName, String businessCode, String code, String language);

	/**
	 * 查询列表 指定code 查询出所有语言环境
	 * @param systemName
	 * @param businessCode
	 * @param code
	 * @return
	 */
	List<I18nItem> selectListByCode(String systemName, String businessCode, String code);

}
