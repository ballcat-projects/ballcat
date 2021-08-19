package com.hccake.ballcat.i18n.service;

import com.hccake.ballcat.i18n.model.dto.I18nDataCreateDTO;
import com.hccake.ballcat.i18n.model.dto.I18nDataDTO;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.model.vo.I18nDataPageVO;
import com.hccake.ballcat.i18n.model.qo.I18nDataQO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-06 10:48:25
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
	 * 查询 i18nData 数据
	 * @param i18nDataQO 查询条件
	 * @return list
	 */
	List<I18nData> queryList(I18nDataQO i18nDataQO);

	/**
	 * 根据国际化标识查询 i18nData 数据
	 * @param code 国际化标识
	 * @return list
	 */
	List<I18nData> listByCode(String code);

	/**
	 * 根据 code 和 languageTag 查询指定的 I18nData
	 * @param code 国际化标识
	 * @param languageTag 语言标签
	 * @return I18nData
	 */
	I18nData getByCodeAndLanguageTag(String code, String languageTag);

	/**
	 * 根据 code 和 languageTag 修改指定的 I18nData
	 * @param i18nDataDTO i18nDataDTO
	 * @return updated true or false
	 */
	boolean updateByCodeAndLanguageTag(I18nDataDTO i18nDataDTO);

	/**
	 * 根据 code 和 languageTag 删除指定的 I18nData
	 * @param code 国际化标识
	 * @param languageTag 语言标签
	 * @return delete true or false
	 */
	boolean removeByCodeAndLanguageTag(String code, String languageTag);

	/**
	 * 保存时跳过已存在的数据
	 * @param list 待保存的 I18nDataList
	 * @return List<I18nData> 已存在的数据
	 */
	List<I18nData> saveWhenNotExist(List<I18nData> list);

	/**
	 * 保存时,若数据已存在，则进行更新
	 * @param list 待保存的 I18nDataList
	 */
	void saveOrUpdate(List<I18nData> list);

	/**
	 * 新建 I18nData
	 * @param i18nDataCreateDTO 创建传输对象
	 * @return if create success return true
	 */
	boolean create(I18nDataCreateDTO i18nDataCreateDTO);

}