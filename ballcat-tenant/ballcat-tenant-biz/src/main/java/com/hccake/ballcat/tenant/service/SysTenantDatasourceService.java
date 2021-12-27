package com.hccake.ballcat.tenant.service;

import com.hccake.ballcat.tenant.model.entity.SysTenantDatasource;
import com.hccake.ballcat.tenant.model.vo.SysTenantDatasourcePageVO;
import com.hccake.ballcat.tenant.model.qo.SysTenantDatasourceQO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 租户数据源映射表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
public interface SysTenantDatasourceService extends ExtendService<SysTenantDatasource> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult&lt;SysTenantDatasourcePageVO&gt; 分页数据
	 */
	PageResult<SysTenantDatasourcePageVO> queryPage(PageParam pageParam, SysTenantDatasourceQO qo);

}