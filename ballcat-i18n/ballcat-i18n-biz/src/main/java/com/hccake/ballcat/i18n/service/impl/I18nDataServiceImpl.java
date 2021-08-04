package com.hccake.ballcat.i18n.service.impl;

import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.i18n.mapper.I18nDataMapper;
import com.hccake.ballcat.i18n.service.I18nDataService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-04 11:31:49
 */
@Service
public class I18nDataServiceImpl extends ExtendServiceImpl<I18nDataMapper, I18nData> implements I18nDataService {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<I18nDataPageVO> 分页数据
	 */
	@Override
	public PageResult<I18nDataPageVO> queryPage(PageParam pageParam, I18nDataQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 * @param code 唯一标识
	 * @param languageTag 语言标识
	 * @return I18nData
	 */
	@Override
	public I18nData getByCodeAndLanguageTag(String code, String languageTag) {
		return baseMapper.selectByCodeAndLanguageTag(code, languageTag);
	}

}
