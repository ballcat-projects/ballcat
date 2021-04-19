package com.hccake.common.i18n.executor;

import com.hccake.common.i18n.model.I18nItem;

import java.util.List;

/**
 * 执行器 主要负责数据提取与附加操作
 *
 * @author Yakir
 */
public interface Executor {

	/**
	 * 指定业务 业务码 语言环境
	 * @param systemName 系统名称
	 * @param businessCode 业务
	 * @param code key
	 * @param language 语言环境
	 * @return 值
	 */
	I18nItem selectOne(String systemName, String businessCode, String code, String language);

	/**
	 * 查询列表 指定code 查询出所有语言环境
	 * @param systemName 系统名称
	 * @param businessCode 业务码
	 * @param code key
	 * @return 值列表
	 */
	List<I18nItem> selectListByCode(String systemName, String businessCode, String code);

}
