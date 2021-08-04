package com.hccake.ballcat.i18n.service;

import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-04 11:31:49
 */
public interface I18nDataService extends ExtendService<I18nData> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult&lt;I18nDataPageVO&gt; 分页数据
	 */
	PageResult<I18nDataPageVO> queryPage(PageParam pageParam, I18nDataQO qo);

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 * @param code 唯一标识
	 * @param languageTag 语言标识
	 * @return I18nData
	 */
	I18nData getByCodeAndLanguageTag(String code, String languageTag);

}