package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.qo.TemplatePropertyQO;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

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
		LambdaQueryWrapperX<TemplateProperty> wrapperX = WrappersX.lambdaAliasQueryX(TemplateProperty.class)
				.eqIfPresent(TemplateProperty::getId, qo.getId())
				.eqIfPresent(TemplateProperty::getGroupId, qo.getGroupId());
		this.selectByPage(page, wrapperX);
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

}