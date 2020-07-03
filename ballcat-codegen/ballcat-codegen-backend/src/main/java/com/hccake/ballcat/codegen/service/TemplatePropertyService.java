package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;

import java.util.List;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
public interface TemplatePropertyService extends IService<TemplateProperty> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	IPage<TemplatePropertyVO> selectPageVo(IPage<?> page, TemplatePropertyQO qo);

	/**
	 * 获取模板组的所有配置
	 * @param templateGroupId 模板组ID
	 * @return List<TemplatePropertyVO> 配置列表
	 */
	List<TemplatePropertyVO> list(Integer templateGroupId);

	/**
	 * 复制模板属性配置
	 * @param resourceId 原模板组ID
	 * @param groupId 模板模板组ID
	 */
	void copy(Integer resourceId, Integer groupId);

}