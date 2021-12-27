package com.hccake.ballcat.tenant.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.tenant.converter.SysTenantConverter;
import com.hccake.ballcat.tenant.model.entity.SysTenant;
import com.hccake.ballcat.tenant.model.qo.SysTenantQO;
import com.hccake.ballcat.tenant.model.vo.SysTenantPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

/**
 * 租户表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
public interface SysTenantMapper extends ExtendMapper<SysTenant> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return PageResult<SysTenantPageVO> VO分页数据
	 */
	default PageResult<SysTenantPageVO> queryPage(PageParam pageParam, SysTenantQO qo) {
		IPage<SysTenant> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<SysTenant> wrapper = WrappersX.lambdaQueryX(SysTenant.class);
		this.selectPage(page, wrapper);
		IPage<SysTenantPageVO> voPage = page.convert(SysTenantConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

}