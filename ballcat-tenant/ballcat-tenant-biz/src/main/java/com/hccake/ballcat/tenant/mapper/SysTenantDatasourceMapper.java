package com.hccake.ballcat.tenant.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.tenant.converter.SysTenantDatasourceConverter;
import com.hccake.ballcat.tenant.model.entity.SysTenantDatasource;
import com.hccake.ballcat.tenant.model.qo.SysTenantDatasourceQO;
import com.hccake.ballcat.tenant.model.vo.SysTenantDatasourcePageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 租户数据源映射表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
public interface SysTenantDatasourceMapper extends ExtendMapper<SysTenantDatasource> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return PageResult<SysTenantDatasourcePageVO> VO分页数据
	 */
	default PageResult<SysTenantDatasourcePageVO> queryPage(PageParam pageParam, SysTenantDatasourceQO qo) {
		IPage<SysTenantDatasource> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<SysTenantDatasource> wrapper = WrappersX.lambdaQueryX(SysTenantDatasource.class);
		this.selectPage(page, wrapper);
		IPage<SysTenantDatasourcePageVO> voPage = page.convert(SysTenantDatasourceConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}