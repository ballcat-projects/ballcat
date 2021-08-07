package com.hccake.ballcat.i18n.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.redis.core.annotation.CacheDel;
import com.hccake.ballcat.common.redis.core.annotation.Cached;
import com.hccake.ballcat.i18n.constant.I18nRedisKeyConstants;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.i18n.mapper.I18nDataMapper;
import com.hccake.ballcat.i18n.service.I18nDataService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-06 10:48:25
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
	@Cached(key = I18nRedisKeyConstants.I18N_DATA_PREFIX, keyJoint = "#code + ':' + #languageTag")
	public I18nData getByCodeAndLanguageTag(String code, String languageTag) {
		return baseMapper.selectByCodeAndLanguageTag(code, languageTag);
	}

	/**
	 * 新建 i18n Data, 此时也应删除对应缓存，因为有可能有空值占位
	 * @param entity 实体对象
	 * @return 添加成功：true
	 */
	@CacheDel(key = I18nRedisKeyConstants.I18N_DATA_PREFIX, keyJoint = "#p0.code + ':' + #p0.languageTag")
	@Override
	public boolean save(I18nData entity) {
		return SqlHelper.retBool(getBaseMapper().insert(entity));
	}

	@Override
	@CacheDel(key = I18nRedisKeyConstants.I18N_DATA_PREFIX, keyJoint = "#p0.code + ':' + #p0.languageTag")
	public boolean updateById(I18nData entity) {
		return SqlHelper.retBool(getBaseMapper().updateById(entity));
	}

	/**
	 * 根据 code 和 languageTag 删除指定的 I18nData
	 * @param code 唯一标识
	 * @param languageTag 语言标签
	 * @return delete true or false
	 */
	@Override
	@CacheDel(key = I18nRedisKeyConstants.I18N_DATA_PREFIX, keyJoint = "#code + ':' + #languageTag")
	public boolean removeByCodeAndLanguageTag(String code, String languageTag) {
		return baseMapper.deleteByCodeAndLanguageTag(code, languageTag);
	}

}
