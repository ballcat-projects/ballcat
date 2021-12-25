package com.hccake.ballcat.tenant.service.impl;

import com.hccake.ballcat.tenant.model.entity.SysTenant;
import com.hccake.ballcat.tenant.model.vo.SysTenantPageVO;
import com.hccake.ballcat.tenant.model.qo.SysTenantQO;
import com.hccake.ballcat.tenant.mapper.SysTenantMapper;
import com.hccake.ballcat.tenant.service.SysTenantService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 租户表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Service
public class SysTenantServiceImpl extends ExtendServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysTenantPageVO> 分页数据
	 */
	@Override
	public PageResult<SysTenantPageVO> queryPage(PageParam pageParam, SysTenantQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
