package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.util.List;

/**
 * 模板属性配置
 *
 * @author hccake
 * @date 2020-06-22 15:46:39
 */
public interface TemplatePropertyMapper extends ExtendMapper<TemplateProperty> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return PageResult<TemplatePropertyVO> 分页数据
	 */
	default PageResult<TemplatePropertyVO> queryPage(PageParam pageParam, TemplatePropertyQO qo) {
		IPage<TemplatePropertyVO> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<TemplateProperty> wrapperX = WrappersX.lambdaQueryX(TemplateProperty.class)
				.eqIfPresent(TemplateProperty::getId, qo.getId())
				.eqIfPresent(TemplateProperty::getGroupId, qo.getGroupId());
		this.selectByPage(page, wrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

	/**
	 * 根据模板组ID获取模板组的所有配置
	 * @param templateGroupId 模板组ID
	 * @return List<TemplateProperty> 配置列表
	 */
	default List<TemplateProperty> listByTemplateGroupId(Integer templateGroupId) {
		return this
				.selectList(Wrappers.<TemplateProperty>lambdaQuery().eq(TemplateProperty::getGroupId, templateGroupId));

	}

	/**
	 * 根据模板组ID 删除模板属性
	 *
	 * @param groupId 模板组ID
	 */
	default void removeByGroupId(Integer groupId){
		this.delete(Wrappers.lambdaQuery(TemplateProperty.class).eq(TemplateProperty::getGroupId, groupId));
	}
}