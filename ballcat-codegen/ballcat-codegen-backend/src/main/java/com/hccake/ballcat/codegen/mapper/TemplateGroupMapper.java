package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.codegen.converter.TemplateModelConverter;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.util.List;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateGroupMapper extends ExtendMapper<TemplateGroup> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询条件
	 * @return PageResult<TemplateGroupVO> 分页数据
	 */
	default PageResult<TemplateGroupPageVO> queryPage(PageParam pageParam, TemplateGroupQO qo) {
		IPage<TemplateGroup> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<TemplateGroup> wrapperX = WrappersX.lambdaQueryX(TemplateGroup.class)
				.eqIfPresent(TemplateGroup::getId, qo.getId());
		this.selectPage(page, wrapperX);
		IPage<TemplateGroupPageVO> voPage = page.convert(TemplateModelConverter.INSTANCE::groupPoToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 获取SelectData数据
	 * @return List<SelectData<?>>
	 */
	List<SelectData<?>> listSelectData();

}