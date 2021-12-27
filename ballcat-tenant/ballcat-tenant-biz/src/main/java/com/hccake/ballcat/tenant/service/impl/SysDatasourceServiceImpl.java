package com.hccake.ballcat.tenant.service.impl;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.tenant.mapper.SysDatasourceMapper;
import com.hccake.ballcat.tenant.model.entity.SysDatasource;
import com.hccake.ballcat.tenant.model.qo.SysDatasourceQO;
import com.hccake.ballcat.tenant.model.vo.SysDatasourcePageVO;
import com.hccake.ballcat.tenant.service.SysDatasourceService;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 数据源表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Service
public class SysDatasourceServiceImpl extends ExtendServiceImpl<SysDatasourceMapper, SysDatasource>
		implements SysDatasourceService {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysDatasourcePageVO> 分页数据
	 */
	@Override
	public PageResult<SysDatasourcePageVO> queryPage(PageParam pageParam, SysDatasourceQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

}
