package com.hccake.ballcat.tenant.service;

import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.tenant.model.entity.SysDatasource;
import com.hccake.ballcat.tenant.model.qo.SysDatasourceQO;
import com.hccake.ballcat.tenant.model.vo.SysDatasourcePageVO;
import com.hccake.extend.mybatis.plus.service.ExtendService;

/**
 * 数据源表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
public interface SysDatasourceService extends ExtendService<SysDatasource> {

	/**
	 * 根据QueryObeject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult&lt;SysDatasourcePageVO&gt; 分页数据
	 */
	PageResult<SysDatasourcePageVO> queryPage(PageParam pageParam, SysDatasourceQO qo);

}