package com.hccake.ballcat.i18n.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.i18n.converter.I18nDataConverter;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-04 11:31:49
 */
public interface I18nDataMapper extends ExtendMapper<I18nData> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return PageResult<I18nDataPageVO> VO分页数据
	 */
	default PageResult<I18nDataPageVO> queryPage(PageParam pageParam, I18nDataQO qo) {
		IPage<I18nData> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<I18nData> wrapper = WrappersX.lambdaQueryX(I18nData.class);
		this.selectPage(page, wrapper);
		IPage<I18nDataPageVO> voPage = page.convert(I18nDataConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 * @param code 唯一标识
	 * @param languageTag 语言标识
	 * @return I18nData
	 */
	default I18nData selectByCodeAndLanguageTag(String code, String languageTag) {
		LambdaQueryWrapper<I18nData> wrapper = Wrappers.lambdaQuery(I18nData.class).eq(I18nData::getCode, code)
				.eq(I18nData::getLanguageTag, languageTag);
		return this.selectOne(wrapper);
	}

}