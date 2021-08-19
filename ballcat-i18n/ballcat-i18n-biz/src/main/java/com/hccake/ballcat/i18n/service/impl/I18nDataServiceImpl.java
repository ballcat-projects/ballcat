package com.hccake.ballcat.i18n.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import com.hccake.ballcat.common.redis.core.annotation.CacheDel;
import com.hccake.ballcat.common.redis.core.annotation.Cached;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.i18n.constant.I18nRedisKeyConstants;
import com.hccake.ballcat.i18n.converter.I18nDataConverter;
import com.hccake.ballcat.i18n.mapper.I18nDataMapper;
import com.hccake.ballcat.i18n.model.dto.I18nDataCreateDTO;
import com.hccake.ballcat.i18n.model.dto.I18nDataDTO;
import com.hccake.ballcat.i18n.model.dto.I18nDataUnique;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.service.I18nDataService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Service
@RequiredArgsConstructor
public class I18nDataServiceImpl extends ExtendServiceImpl<I18nDataMapper, I18nData> implements I18nDataService {

	private final StringRedisTemplate stringRedisTemplate;

	private final I18nDataTxSupport i18NDataTxSupport;

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
	 * 查询 i18nData 数据
	 * @param i18nDataQO 查询条件
	 * @return list
	 */
	@Override
	public List<I18nData> queryList(I18nDataQO i18nDataQO) {
		return baseMapper.queryList(i18nDataQO);
	}

	@Override
	public List<I18nData> listByCode(String code) {
		return baseMapper.listByCode(code);
	}

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 * @param code 唯一标识
	 * @param languageTag 语言标签
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

	/**
	 * 根据 code 和 languageTag 修改指定的 I18nData
	 * @param i18nDataDTO i18nData 实体对象
	 * @return updated true or false
	 */
	@Override
	@CacheDel(key = I18nRedisKeyConstants.I18N_DATA_PREFIX, keyJoint = "#p0.code + ':' + #p0.languageTag")
	public boolean updateByCodeAndLanguageTag(I18nDataDTO i18nDataDTO) {
		boolean updateSuccess = baseMapper.updateByCodeAndLanguageTag(i18nDataDTO);
		if (updateSuccess) {
			pushUpdateMessage(i18nDataDTO.getCode(), i18nDataDTO.getLanguageTag());
		}
		return updateSuccess;
	}

	/**
	 * 根据 code 和 languageTag 删除指定的 I18nData
	 * @param code 国际化标识
	 * @param languageTag 语言标签
	 * @return delete true or false
	 */
	@Override
	@CacheDel(key = I18nRedisKeyConstants.I18N_DATA_PREFIX, keyJoint = "#code + ':' + #languageTag")
	public boolean removeByCodeAndLanguageTag(String code, String languageTag) {
		boolean deleteSuccess = baseMapper.deleteByCodeAndLanguageTag(code, languageTag);
		if (deleteSuccess) {
			pushUpdateMessage(code, languageTag);
		}
		return deleteSuccess;
	}

	@Override
	public List<I18nData> saveWhenNotExist(List<I18nData> list) {
		// 查询已存在的数据
		List<I18nData> existsI18nData = baseMapper.exists(list);
		// 删除已存在的数据
		list.removeAll(existsI18nData);
		// 落库
		if (CollectionUtil.isNotEmpty(list)) {
			baseMapper.insertBatchSomeColumn(list);
		}
		return existsI18nData;
	}

	@Override
	public void saveOrUpdate(List<I18nData> list) {
		// 查询已存在的数据
		List<I18nData> existsI18nData = baseMapper.exists(list);
		HashSet<I18nData> existsSet = new HashSet<>(existsI18nData);

		// 获取对应插入和更新的列表
		List<I18nDataDTO> updateList = new ArrayList<>();
		List<I18nData> insertList = new ArrayList<>();
		for (I18nData i18nData : list) {
			if (existsSet.contains(i18nData)) {
				updateList.add(I18nDataConverter.INSTANCE.poToDto(i18nData));
			}
			else {
				insertList.add(i18nData);
			}
		}
		// 小范围事务处理，另外不影响缓存更新
		i18NDataTxSupport.saveAndUpdate(insertList, updateList);

		// 缓存更新
		String delimiter = CachePropertiesHolder.delimiter();
		for (I18nDataDTO i18nDataDTO : updateList) {
			String code = i18nDataDTO.getCode();
			String languageTag = i18nDataDTO.getLanguageTag();
			String key = String.join(delimiter, I18nRedisKeyConstants.I18N_DATA_PREFIX, code + ":" + languageTag);
			stringRedisTemplate.delete(key);
			this.pushUpdateMessage(code, languageTag);
		}
	}

	@Override
	public boolean create(I18nDataCreateDTO i18nDataCreateDTO) {
		// 转换为实体类列表
		List<I18nData> list = new ArrayList<>();
		List<I18nDataCreateDTO.LanguageText> languageTexts = i18nDataCreateDTO.getLanguageTexts();
		for (I18nDataCreateDTO.LanguageText languageText : languageTexts) {
			I18nData i18nData = new I18nData();
			i18nData.setCode(i18nDataCreateDTO.getCode());
			i18nData.setRemark(i18nDataCreateDTO.getRemark());
			i18nData.setLanguageTag(languageText.getLanguageTag());
			i18nData.setMessage(languageText.getMessage());
			list.add(i18nData);
		}
		// 落库存储
		int insertFlag = baseMapper.insertBatchSomeColumn(list);
		return SqlHelper.retBool(insertFlag);
	}

	/**
	 * 通过 redis 推送 i18nData 变更消息
	 * @param code 国际化标识
	 * @param languageTag 语言标签
	 */
	private void pushUpdateMessage(String code, String languageTag) {
		I18nDataUnique channelBody = new I18nDataUnique(code, languageTag);
		String str = JsonUtils.toJson(channelBody);
		stringRedisTemplate.convertAndSend(I18nRedisKeyConstants.CHANNEL_I18N_DATA_UPDATED, str);
	}

	@Component
	@AllArgsConstructor
	static class I18nDataTxSupport {

		private final I18nDataMapper i18nDataMapper;

		@Transactional(rollbackFor = Exception.class)
		public void saveAndUpdate(List<I18nData> insertList, List<I18nDataDTO> updateList) {
			// 插入不存的数据
			if (CollectionUtil.isNotEmpty(insertList)) {
				i18nDataMapper.insertBatchSomeColumn(insertList);
			}
			// 更新已有数据
			for (I18nDataDTO i18nDataDTO : updateList) {
				i18nDataMapper.updateByCodeAndLanguageTag(i18nDataDTO);
			}
		}

	}

}
