package com.hccake.ballcat.tenant.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.tenant.converter.SysDatasourceConverter;
import com.hccake.ballcat.tenant.model.entity.SysDatasource;
import com.hccake.ballcat.tenant.model.qo.SysDatasourceQO;
import com.hccake.ballcat.tenant.model.vo.SysDatasourcePageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 数据源表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
public interface SysDatasourceMapper extends ExtendMapper<SysDatasource> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return PageResult<SysDatasourcePageVO> VO分页数据
	 */
	default PageResult<SysDatasourcePageVO> queryPage(PageParam pageParam, SysDatasourceQO qo) {
		IPage<SysDatasource> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<SysDatasource> wrapper = WrappersX.lambdaQueryX(SysDatasource.class);
		this.selectPage(page, wrapper);
		IPage<SysDatasourcePageVO> voPage = page.convert(SysDatasourceConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}