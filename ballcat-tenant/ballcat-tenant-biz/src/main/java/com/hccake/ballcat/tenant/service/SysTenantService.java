package com.hccake.ballcat.tenant.service;

import com.hccake.ballcat.tenant.model.entity.SysTenant;
import com.hccake.ballcat.tenant.model.vo.SysTenantPageVO;
import com.hccake.ballcat.tenant.model.qo.SysTenantQO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 租户表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
public interface SysTenantService extends ExtendService<SysTenant> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult&lt;SysTenantPageVO&gt; 分页数据
	 */
	PageResult<SysTenantPageVO> queryPage(PageParam pageParam, SysTenantQO qo);

}